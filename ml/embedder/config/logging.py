import logging

def setup_logging():
    logging.basicConfig(
        level=logging.INFO,
        format="[%(levelname)s]: %(name)s - %(message)s",
    )
    return logging.getLogger("ml/embedder")