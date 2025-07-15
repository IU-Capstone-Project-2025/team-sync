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
        if response.status_code != 200:
            logger.error(f"Embedder service returned error: {response.text}")
        response = response.json()
        embeddings = response.get("embeddings", [])
        return [embedding for embedding in embeddings if isinstance(embedding, list)]
