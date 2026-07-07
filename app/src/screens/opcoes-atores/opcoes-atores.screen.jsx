import React from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { useIsMobile } from "../../hooks/use-is-mobile/use-is-mobile.hook";
import OpcoesAtoresMobileLayout from "./layouts/opcoes-atores-mobile.layout";
import OpcoesAtoresWebLayout from "./layouts/opcoes-atores-web.layout";

export function OpcoesAtoresScreen() {
  const location = useLocation();
  const navigate = useNavigate();
  const isMobile = useIsMobile();
  const { opcoes = [] } = location.state || {};

  const abrirAtor = (ator) => {
    navigate(`/ator/${ator.id}`);
  };

  const layoutProps = {
    opcoes,
    abrirAtor,
  };

  return isMobile ? (
    <OpcoesAtoresMobileLayout {...layoutProps} />
  ) : (
    <OpcoesAtoresWebLayout {...layoutProps} />
  );
}
