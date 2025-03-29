import faiss
import os
import pickle
import numpy as np

BASE_DIR = os.path.dirname(os.path.abspath(__file__))
OUTPUT_FILE = f"{BASE_DIR}/embeddings/embed_combinado.pkl"

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

def build_faiss(data):
    embeddings = []
    labels = []

    for classe, vetor_list in data.items():
        for vetor in vetor_list:
            embeddings.append(vetor)
            labels.append(classe)

    embeddings = np.array(embeddings).astype(np.float32)
    faiss.normalize_L2(embeddings)

    d = embeddings.shape[1]
    index = faiss.IndexFlatL2(d)

    index.add(embeddings)
    faiss.write_index(index, "faiss_index.bin")

    with open("faiss_labels.pkl", "wb") as f:
        pickle.dump(labels, f)

    print(f"Índice criado com {index.ntotal} embeddings")

def print_embeddings_info():
    maior, menor = 0, 0
    embeddings = load_embeddings()
    
    for _, emb_list in embeddings.items():
        if len(emb_list) >= 3:
            maior += 1
        else:
            menor += 1
    print(maior, menor)

embeddings = load_embeddings()
centroids = calculate_centroids(embeddings)
save_centroids(centroids)
build_faiss(embeddings)
print_embeddings_info()
