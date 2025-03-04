import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { useGetProducoesPorNome } from "../../api/hooks/useGetProducoesPorNome/useGetProducoesPorNome.hook";
import AtorInfo from "../../components/ator-info/ator-info.component";
import "./exibir-producoes.css";
import Loading from "../../components/loading/loading.component";
import { useSwipeable } from "react-swipeable";
import Tab from "../../components/tab/tab.component";
import ListProducoes from "../../components/list-producoes/list-producoes.component";
import Cabecalho from "../../components/cabecalho/cabecalho.component";

export function ExibirProducoesScreen() {
  const { producoes, getProducoesPorNomeFunc } = useGetProducoesPorNome();
  const { name } = useParams();
  const [tab, setTab] = useState("FILME");

  const handleSwipe = useSwipeable({
    onSwipedLeft: () => setTab("TV"),
    onSwipedRight: () => setTab("FILME"),
    trackMouse: true,
  });

  useEffect(() => {
    getProducoesPorNomeFunc(name);
  }, [name, getProducoesPorNomeFunc]);

  return (
    <div className="container">
      {producoes === null ? (
        <Loading />
      ) : (
        <div className="container-producoes">
          <Cabecalho />
          <div className="ator-info-container">
            <AtorInfo nome={producoes.nome} imagem={producoes.urlFoto} />
          </div>
          <Tab setTab={setTab} estado={tab} tabs={["FILME", "TV"]} />
          <ListProducoes
            producoes={producoes}
            handleSwipe={handleSwipe}
            tab={tab}
          />
        </div>
      )}
    </div>
  );
}
