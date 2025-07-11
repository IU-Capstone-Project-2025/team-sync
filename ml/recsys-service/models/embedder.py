import requests

class Embedder:
    def __init__(self, url: str, logger=None, timeout: int = 5):
        self.url = url
        self.logger = logger
        self.timeout = timeout

    def encode(self, texts):
        """Returns embeddings"""
        logger = self.logger
        response = requests.post(f"{self.url}/api/v1/embedding", json={"texts": texts}, timeout=self.timeout)
        logger.info(f"Embedder response: {response.status_code}")
        response = response.json()
        embeddings = response.get("embeddings", [])
        logger.info(f"Received {len(embeddings)} embeddings from embedder.")
        return [embedding for embedding in embeddings if isinstance(embedding, list)]
