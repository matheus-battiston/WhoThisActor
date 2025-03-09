import faiss
import os
import pickle
import numpy as np

BASE_DIR = os.path.dirname(os.path.abspath(__file__))
EMBEDDINGS_FILES = ["embed512v3.pkl", "embed512v4.pkl"]  # Adicione os arquivos aqui
INDEXES_DIR = os.path.join(BASE_DIR, "faiss_indexes")
os.makedirs(INDEXES_DIR, exist_ok=True)

# Função para carregar e combinar embeddings de múltiplos arquivos
def load_and_merge_embeddings():
    combined_embeddings = {}
    
    for file_path in EMBEDDINGS_FILES:
        try:
            with open(file_path, "rb") as f:
                embeddings, _ = pickle.load(f)
                for label, emb_list in embeddings.items():
                    if label in combined_embeddings:
                        combined_embeddings[label].extend(emb_list)
                    else:
                        combined_embeddings[label] = emb_list
        except Exception as e:
            print(f"Erro ao carregar embeddings de {file_path}: {e}")
    
    return combined_embeddings

# Função para calcular o centroide de cada classe
def calculate_centroids(embeddings):
    centroids = {}
    for label, emb_list in embeddings.items():
        centroids[label] = np.mean(emb_list, axis=0)
    return centroids

# Função para salvar centroides
def save_centroids(centroids):
    try:
        with open("centroids.pkl", "wb") as f:
            pickle.dump(centroids, f)
        print("Centroides calculados e salvos com sucesso.")
    except Exception as e:
        print(f"Erro ao salvar os centroides: {e}")

# Construção do índice FAISS
def build_faiss_index():
    embeddings = load_and_merge_embeddings()
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
    
    print("Índices FAISS construídos e salvos com sucesso.")

# Informações sobre os embeddings
def print_embeddings_info():
    maior, menor = 0, 0
    embeddings = load_and_merge_embeddings()
    
    for label, emb_list in embeddings.items():
        if len(emb_list) >= 3:
            maior += 1
        else:
            menor += 1
    print(maior, menor)

# Executando as funções
# embeddings = load_and_merge_embeddings()
# centroids = calculate_centroids(embeddings)
# save_centroids(centroids)
# build_faiss_index()
print_embeddings_info()
