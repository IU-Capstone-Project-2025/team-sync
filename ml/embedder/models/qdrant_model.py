from qdrant_client import QdrantClient
from config.config import Config

class QdrantModel:
    def __init__(self, url: str = Config().QDRANT_URL, 
                 api_key: str = Config().QDRANT_API_KEY, embedding_shape = 384, logger=None):
        self.client = QdrantClient(url=url, api_key=api_key)
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

    def update_embeddings(self, collection_name, ids, embeddings):
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

    def get_similarities(self, collection_name: str, init_id: int, among_projects = False):
        collection_name_2 = collection_name
        if among_projects:
            collection_name_2 = "project"
        info = self.client.get_collection(collection_name)
        if info.points_count == 0:
            return []
        point = self.client.retrieve(collection_name, [init_id], with_vectors=True)
        if not point or not hasattr(point[0], 'vector') or point[0].vector is None:
            return []
        info_2 = self.client.get_collection(collection_name_2)
        if info_2.points_count == 0:
            return []
        query_vector = point[0].vector
        result = self.client.search(
            collection_name=collection_name_2,
            query_vector=query_vector,
            limit=info_2.points_count
        )
        result = [{"table": collection_name_2, "id": res.id, "score": res.score} for res in result]
        return result
    
    
