from pydantic import BaseModel
from typing import List, Union

class EmbeddingRequest(BaseModel):
    texts: Union[str, List[str]]