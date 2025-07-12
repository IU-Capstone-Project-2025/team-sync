from pydantic import BaseModel
from typing import Literal

class QdrantEmbeddingRequest(BaseModel):
    id: int
    table: Literal["student", "project"]
