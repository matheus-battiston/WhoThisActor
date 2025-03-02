import faiss
import os
import pickle
import numpy as np

BASE_DIR = os.path.dirname(os.path.abspath(__file__))
EMBEDDINGS_FILE_PATH = "embeddings_combinados.pkl"
INDEXES_DIR = os.path.join(BASE_DIR, "faiss_indexes")
os.makedirs(INDEXES_DIR, exist_ok=True)

def load_embeddings_from_file():
    try:
        with open(EMBEDDINGS_FILE_PATH, "rb") as f:
            embeddings, labels = pickle.load(f)
        return embeddings, labels
    except Exception as e:
        print(f"Erro ao carregar os embeddings do arquivo: {e}")
        return {}, {}

def build_faiss_index():
    global embeddings, labels
    embeddings, labels = load_embeddings_from_file()
    
    faiss_indices = {}

    for label, emb_list in embeddings.items():
        if not emb_list:
            continue

        emb_array = np.array(emb_list, dtype=np.float32)        
        faiss.normalize_L2(emb_array)

        n_neighbors = 16  
        index = faiss.IndexHNSWFlat(emb_array.shape[1], n_neighbors)
    
        index.add(emb_array)
        faiss_indices[label] = index
        
        # Salvar o índice de cada classe em um arquivo separado
        faiss.write_index(index, os.path.join(INDEXES_DIR, f"{label}_index.idx"))

build_faiss_index()