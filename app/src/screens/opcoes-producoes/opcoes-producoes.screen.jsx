import React from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { useIsMobile } from "../../hooks/use-is-mobile/use-is-mobile.hook";
import OpcoesProducoesMobileLayout from "./layouts/opcoes-producoes-mobile.layout";
import OpcoesProducoesWebLayout from "./layouts/opcoes-producoes-web.layout";

export function OpcoesProducoesScreen() {
  const navigate = useNavigate();
  const location = useLocation();
  const { opcoes = [], tipoMidia } = location.state || {};
  const isMobile = useIsMobile();

  const mapearProducao = (item) => {
    return {
      id: item.id,
      nomeProducao: item.nome,
      posterLink: item.imagem,
      nomePersonagem: "",
    };
  };

  const mapearTipo = (item) =>
    (item.tipoMidia || tipoMidia) === "MOVIE" ? "FILME" : "SERIE";

  const obterTipoMidia = (item) => item.tipoMidia || tipoMidia || "TV";

  const abrirProducao = (item) => {
    navigate(`/exibirElenco/${obterTipoMidia(item)}/${item.id}`);
  };

  const tipoReferencia = tipoMidia || opcoes?.[0]?.tipoMidia || "TV";
  const textoTipo = tipoReferencia === "MOVIE" ? "filme" : "série";
  const artigoTipo = tipoReferencia === "MOVIE" ? "um" : "uma";
  const artigoSelecao = tipoReferencia === "MOVIE" ? "o" : "a";

  return isMobile ? (
    <OpcoesProducoesMobileLayout
      opcoes={opcoes}
      tipoMidia={tipoMidia}
      abrirProducao={abrirProducao}
      introTitle={`Encontramos mais de ${artigoTipo} ${textoTipo} com esse nome`}
      introSubtitle={`Selecione ${artigoSelecao} ${textoTipo} que você deseja.`}
    />
  ) : (
    <OpcoesProducoesWebLayout
      opcoes={opcoes}
      mapearProducao={mapearProducao}
      mapearTipo={mapearTipo}
    />
  );
}
