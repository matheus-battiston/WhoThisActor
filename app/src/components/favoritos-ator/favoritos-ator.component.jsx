import "./favoritos-ator.css";
import ListaProducoesFavoritasComponent from "../lista-producoes-favoritas/lista-producoes-favoritas.component";

export const FavoritosAtor = ({ atoresFavoritos }) => {
  return (
    <ListaProducoesFavoritasComponent itens={atoresFavoritos} tipo="ATOR" />
  );
};
