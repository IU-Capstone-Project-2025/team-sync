from qdrant_client import QdrantClient
from config.config import Config

class QdrantModel:
    def __init__(self, host: str = Config.QDRANT_HOST, port: int = Config.QDRANT_PORT, 
                 api_key: str = Config().QDRANT_API_KEY, embedding_shape = 384, logger=None):
        self.client = QdrantClient(url=f"http://{host}:{port}", api_key=api_key)
        self.logger = logger
        self.create_collection("project", embedding_shape=embedding_shape)
        self.create_collection("student", embedding_shape=embedding_shape)

    def create_collection(self, collection_name: str, embedding_shape: int = None, distance = "Cosine"):
        existing = [col.name for col in self.client.get_collections().collections]
        if collection_name not in existing:
            self.client.create_collection(collection_name, vectors_config={"size": embedding_shape, "distance": distance})
            self.logger.info(f"Collection '{collection_name}' created successfully.")
        else:
            self.logger.info(f"Collection '{collection_name}' already exists, skipping creation.")

    def add_embeddings(self, collection_name, ids, embeddings):
        if not isinstance(ids, list):
            ids = [ids]
        if not isinstance(embeddings, list):
            embeddings = [embeddings]
        self.client.upsert(
            collection_name=collection_name,
            points=[
                {"id": id, "vector": doc} for id, doc in zip(ids, embeddings)
            ]
        )

    def get_embeddings(self, collection_name: str, ids):
        if not isinstance(ids, list):
            ids = [ids]
        return self.client.retrieve(collection_name, ids, with_vectors=True)
