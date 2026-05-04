import React, { useState } from "react";
import Cabecalho from "../../components/cabecalho/cabecalho.component";

import "./favoritos.css";
import Tab from "../../components/tab/tab.component";
import { FavoritosProducao } from "../../components/favoritos-producao/favoritos-producao.component";
import { FavoritosAtor } from "../../components/favoritos-ator/favoritos-ator.component";
import Loading from "../../components/loading/loading.component";
import {
  useListarAtoresFavoritos,
  useListarProducoesFavoritas,
} from "../../api/generated/api";

export default function FavoritosScreen() {
  const [tipo, setTipo] = useState("PRODUÇÃO");
  const FRASE_CARREGAMENTO = "Carregando favoritos";
  const atoresFavoritos = useListarAtoresFavoritos({
    query: { enabled: true },
  });

  const producoesFavoritas = useListarProducoesFavoritas({
    query: { enabled: true },
  });

  const loading = producoesFavoritas.isLoading || producoesFavoritas.isLoading;

  return (
    <div className="page-favoritos">
      {loading ? (
        <Loading frase={FRASE_CARREGAMENTO} />
      ) : (
        <>
          <Cabecalho />
          <div className="container-favoritos">
            <h1 className="title-favoritos">Favoritos</h1>
            <Tab
              setTab={setTipo}
              estado={tipo}
              tabs={["PRODUÇÃO", "ATORES"]}
              tamanho="pequeno"
            />
            {tipo === "PRODUÇÃO" ? (
              <FavoritosProducao
                producoesFavoritas={producoesFavoritas.data?.data}
              />
            ) : (
              <FavoritosAtor atoresFavoritos={atoresFavoritos.data?.data} />
            )}
          </div>
        </>
      )}
    </div>
  );
}
