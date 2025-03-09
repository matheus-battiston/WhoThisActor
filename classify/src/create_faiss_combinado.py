import faiss
import os
import pickle
import numpy as np

BASE_DIR = os.path.dirname(os.path.abspath(__file__))
EMBEDDINGS_FILES = ["embed512v3.pkl", "embed512v4.pkl"]  # Lista de arquivos a serem combinados
INDEXES_DIR = os.path.join(BASE_DIR, "faiss_indexes")
os.makedirs(INDEXES_DIR, exist_ok=True)

def load_embeddings_from_files():
    combined_embeddings = {}
    combined_labels = {}

    for file in EMBEDDINGS_FILES:
        try:
            with open(file, "rb") as f:
                embeddings, labels = pickle.load(f)

            for label, emb_list in embeddings.items():
                if label not in combined_embeddings:
                    combined_embeddings[label] = []
                    combined_labels[label] = []

                combined_embeddings[label].extend(emb_list)
                combined_labels[label].extend(labels[label])

        except Exception as e:
            print(f"Erro ao carregar os embeddings do arquivo {file}: {e}")

    return combined_embeddings, combined_labels

def build_faiss_index():
    embeddings, labels = load_embeddings_from_files()
    
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
        
        faiss.write_index(index, os.path.join(INDEXES_DIR, f"{label}_index.idx"))

build_faiss_index()
