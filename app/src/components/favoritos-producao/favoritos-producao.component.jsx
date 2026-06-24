import { useState } from "react";
import { PillToggle } from "../pill-toggle/pill-toggle.component";
import ListaProducoesFavoritasComponent from "../lista-producoes-favoritas/lista-producoes-favoritas.component";

export const FavoritosProducao = ({ producoesFavoritas }) => {
  const [tipoProducao, setTipoProducao] = useState("series");

  return (
    <div>
      <PillToggle
        options={[
          { label: "Séries", value: "series" },
          { label: "Filmes", value: "filmes" },
        ]}
        value={tipoProducao}
        onChange={setTipoProducao}
      />
      <ListaProducoesFavoritasComponent
        itens={
          tipoProducao === "filmes"
            ? producoesFavoritas.filmes
            : producoesFavoritas.series
        }
        tipo="PRODUÇÃO"
        tipoMidia={tipoProducao === "filmes" ? "MOVIE" : "TV"}
      />
    </div>
  );
};
