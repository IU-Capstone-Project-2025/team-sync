from qdrant_client import QdrantClient
from config.config import Config
from typing import Optional
import requests

class QdrantModel:
    def __init__(self, url: str = None, logger=None):
        self.config = Config()
        if not url:
            url = self.config.QDRANT_URL
        if not url:
            raise ValueError("Qdrant URL is not set in the configuration.")
        self.client = QdrantClient(url=url, api_key=self.config.QDRANT_API_KEY)
        self.logger = logger

    def _create_collection(self, collection_name: str, embedding_shape: int = None, distance = "Cosine"):
        existing = [col.name for col in self.client.get_collections().collections]
        if collection_name not in existing:
            self.client.create_collection(collection_name, vectors_config={"size": embedding_shape, "distance": distance})
            self.logger.info(f"Collection '{collection_name}' created successfully.")
        else:
            self.logger.info(f"Collection '{collection_name}' already exists, skipping creation.")

    def collection_exists(self, collection_name: str) -> bool:
        """Check if a collection exists in Qdrant."""
        existing = [col.name for col in self.client.get_collections().collections]
        self.logger.info(f"Checking existence of collection '{collection_name}'.")
        if not collection_name or not isinstance(collection_name, str):
            self.logger.warning("Invalid collection name provided.")
            if collection_name in ["project", "student"]:
                self.logger.info(f"Creating default collection '{collection_name}'.")
                self._create_collection(collection_name)
            return False
        return collection_name in existing

    def _get_embedding(self, collection_name: str, id) -> Optional[list[float]]:
        if not self.collection_exists(collection_name):
            self.logger.warning(f"Collection '{collection_name}' does not exist.")
            return None
        if id is None or not (isinstance(id, int) or isinstance(id, str)):
            self.logger.warning("ID is None or invalid, cannot retrieve embedding.")
            return None
        emb = self.client.retrieve(collection_name, [id], with_vectors=True)
        if not emb or len(emb) == 0:
            self.logger.warning(f"No embedding found for ID {id} in collection '{collection_name}'.")
            return None
        record = emb[0]
        if hasattr(record, 'vector'):
            result = list(record.vector) if record.vector is not None else []
        else:
            result = []
        if not result:
            self.logger.warning(f"No embedding found for ID {id} in collection '{collection_name}'.")
            return None
        return result

    def _update_embeddings(self, collection_name, id) -> bool:
        request_data = {"id": id, "table": collection_name}
        response = requests.post(f"{self.config.EMBEDDER_URL}/api/v1/points", json=request_data, timeout=5)
        if response.status_code != 200:
            self.logger.error(f"Embedder service returned error: {response.text}")
            return False
        response_data = response.json()
        self.logger.info(f"Updated embeddings for {collection_name} with ID {id}.")
        self.logger.info(f"Response data: {response_data}")
        return True


    def get_embedding(self, collection_name: str, id) -> Optional[list[float]]:
        """Retrieve embeddings for a given ID from a specified collection.
        
        If the embeddings are missing, the method attempts to update them
        using the embedder service and retrieves them again.
        """
        if not self.collection_exists(collection_name):
            self.logger.warning(f"Collection '{collection_name}' does not exist.")
            return None
        if not (isinstance(id, int) or isinstance(id, str)):
            self.logger.warning("ID is None or invalid, cannot check existence.")
            return None
        emb = self._get_embedding(collection_name, id)
        exists = emb is not None and len(emb) > 0
        if not exists:
            self.logger.warning(f"ID {id} does not exist in collection '{collection_name}'.")
            if not self._update_embeddings(collection_name, id):
                self.logger.error(f"Failed to update embeddings for {collection_name} with ID {id}.")
                return None
            emb = self._get_embedding(collection_name, id)
        if emb is None:
            self.logger.error(f"Failed to retrieve embedding for {collection_name} with ID {id}.")
            return None
        return emb