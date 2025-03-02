import pickle
import io
from azure.storage.blob import BlobServiceClient
from PIL import Image
from io import BytesIO
from retrying import retry
import time


class EmbeddingService:
    def __init__(self, connection_string: str, container_name: str, blob_name: str):
        self.connection_string = connection_string
        self.container_name = container_name
        self.blob_name = blob_name

    def load_embeddings_from_blob(self):
        start_time = time.time()  # Início da medição de tempo
        try:
            # Conectar ao Blob Storage
            blob_service_client = BlobServiceClient.from_connection_string(self.connection_string)
            container_client = blob_service_client.get_container_client(self.container_name)
            blob_client = container_client.get_blob_client(self.blob_name)

            # Baixar os dados do Blob
            blob_data = blob_client.download_blob().readall()

            # Carregar embeddings com pickle
            embeddings, label = pickle.load(io.BytesIO(blob_data))
            end_time = time.time()  # Fim da medição de tempo
            print(f"load_embeddings_from_blob execução: {end_time - start_time:.4f} segundos")
            return embeddings, label
        except Exception as e:
            end_time = time.time()  # Fim da medição de tempo
            print(f"Erro ao carregar embeddings do Blob: {e}")
            print(f"load_embeddings_from_blob erro execução: {end_time - start_time:.4f} segundos")
            return None
    
    @retry(stop_max_attempt_number=3, wait_fixed=2000)
    def load_image_from_blob(self):
        start_time = time.time()  # Início da medição de tempo
        # Cria o cliente de serviço de blob
        blob_service_client = BlobServiceClient.from_connection_string(self.connection_string)
        
        # Obtém o cliente do container
        container_client = blob_service_client.get_container_client(self.container_name)
        
        # Obtém o cliente do blob
        blob_client = container_client.get_blob_client(self.blob_name)
        
        # Baixa o conteúdo do blob e retorna os dados em memória
        blob_data = blob_client.download_blob()
        imagem_bytes = blob_data.readall()
        
        end_time = time.time()  # Fim da medição de tempo
        print(f"load_image_from_blob execução: {end_time - start_time:.4f} segundos")
        return imagem_bytes
    
    def delete_blob(self):
        start_time = time.time()  # Início da medição de tempo
        try:
            # Crie uma instância do BlobServiceClient
            blob_service_client = BlobServiceClient.from_connection_string(self.connection_string)

            # Acesse o container onde as imagens estão armazenadas
            container_client = blob_service_client.get_container_client(self.container_name)

            # Acesse o blob que precisa ser deletado
            blob_client = container_client.get_blob_client(self.blob_name)

            # Exclua o blob
            blob_client.delete_blob()

            end_time = time.time()  # Fim da medição de tempo
            print(f"delete_blob execução: {end_time - start_time:.4f} segundos")
        except Exception as e:
            end_time = time.time()  # Fim da medição de tempo
            print(f'Erro ao excluir o blob: {str(e)}')
            print(f"delete_blob erro execução: {end_time - start_time:.4f} segundos")