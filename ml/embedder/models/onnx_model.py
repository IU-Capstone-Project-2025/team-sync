from config.config import Config
import numpy as np
import onnxruntime as ort
from tokenizers import Tokenizer

class ONNXInference:
    def __init__(self, model_path: str = Config.MODEL_PATH, 
                 tokenizer_path: str = Config.TOKENIZER_PATH):
        self.session = ort.InferenceSession(model_path)
        self.tokenizer = Tokenizer.from_file(tokenizer_path)
        self.tokenizer.enable_padding(pad_id=0, pad_token="[PAD]")
        self.tokenizer.enable_truncation(max_length=512)
        self.shape = self.session.get_inputs()[0].shape

    def l2_normalize(self, embeddings):
        norms = np.linalg.norm(embeddings, axis=1, keepdims=True)
        norms = np.maximum(norms, 1e-12)
        normalized = embeddings / norms
        return normalized

    def encode_texts(self, texts, max_length=512):
        if isinstance(texts, str):
            texts = [texts]
        
        input_ids = []
        attention_mask = []
        token_type_ids = []
        
        for text in texts:
            encoding = self.tokenizer.encode(text)
            ids = encoding.ids
            mask = encoding.attention_mask
            type_ids = [0] * len(ids)
            if len(ids) > max_length:
                ids = ids[:max_length]
                mask = mask[:max_length]
                type_ids = type_ids[:max_length]
            else:
                padding_length = max_length - len(ids)
                ids += [0] * padding_length
                mask += [0] * padding_length
                type_ids += [0] * padding_length
            
            input_ids.append(ids)
            attention_mask.append(mask)
            token_type_ids.append(type_ids)
        
        return {
            'input_ids': np.array(input_ids, dtype=np.int64),
            'attention_mask': np.array(attention_mask, dtype=np.int64),
            'token_type_ids': np.array(token_type_ids, dtype=np.int64)
        }
    
    def get_embeddings(self, texts):
        inputs = self.encode_texts(texts)
        
        outputs = self.session.run(None, inputs)
        last_hidden_state = outputs[0]
        
        attention_mask = inputs['attention_mask']
        attention_mask_expanded = np.expand_dims(attention_mask, axis=-1)
        attention_mask_expanded = np.broadcast_to(
            attention_mask_expanded, 
            last_hidden_state.shape
        ).astype(np.float32)
        
        sum_embeddings = np.sum(last_hidden_state * attention_mask_expanded, axis=1)
        sum_mask = np.sum(attention_mask_expanded, axis=1)
        sum_mask = np.maximum(sum_mask, 1e-9)
        mean_pooled = sum_embeddings / sum_mask
        normalized_embeddings = self.l2_normalize(mean_pooled)
        return normalized_embeddings
    
    def compute_similarity(self, texts1, texts2=None):
        if texts2 is None:
            embeddings1 = self.get_embeddings(texts1)
            return np.dot(embeddings1, embeddings1.T)
        else:
            embeddings1 = self.get_embeddings(texts1)
            embeddings2 = self.get_embeddings(texts2)
            return np.dot(embeddings1, embeddings2.T)
