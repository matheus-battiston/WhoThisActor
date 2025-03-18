import faiss
import os
import pickle
import numpy as np

BASE_DIR = os.path.dirname(os.path.abspath(__file__))
EMBEDDINGS_FILES = [f"{BASE_DIR}/embeddings/embed512v3.pkl",f"{BASE_DIR}/embeddings/embed512v4.pkl"]  # Lista de arquivos a serem combinados
OUTPUT_FILE = f"{BASE_DIR}/embeddings/embeddings_combinados.pkl"

def load_and_save_embeddings():
    combined_embeddings = {}

    for file in EMBEDDINGS_FILES:
        try:
            with open(file, "rb") as f:
                embeddings, _ = pickle.load(f)  # Ignora os labels

            for label, emb_list in embeddings.items():
                if label not in combined_embeddings:
                    combined_embeddings[label] = []
                combined_embeddings[label].extend(emb_list)

        except Exception as e:
            print(f"Erro ao carregar os embeddings do arquivo {file}: {e}")

    # Salvar os embeddings combinados
    with open(OUTPUT_FILE, "wb") as f:
        pickle.dump(combined_embeddings, f)
    print(f"Embeddings combinados salvos em {OUTPUT_FILE}")

def load_embeddings():
    combined_embeddings = {}

    try:
        with open(OUTPUT_FILE, "rb") as f:
            embeddings = pickle.load(f)

        for label, emb_list in embeddings.items():
            if label not in combined_embeddings:
                combined_embeddings[label] = []
            combined_embeddings[label].extend(emb_list)

    except Exception as e:
        print(f"Erro ao carregar os embeddings do arquivo {OUTPUT_FILE}: {e}")

    return combined_embeddings

load_and_save_embeddings()
