import React, { useEffect, useState } from "react";
import Logo from "../../components/logo/logo.component";
import { useNavigate } from "react-router-dom";
import "./home-screen.css";
import { useNomeAtor } from "../../api/hooks/useNomeAtor/useNomeAtor.hook";
import { useNomeSerie } from "../../api/hooks/useNomeSerie/useNomeSerie.hook";
import PesquisaAtor from "../../components/pesquisa-ator/pesquisa-ator.component";
import Tab from "../../components/tab/tab.component";
import PesquisaElenco from "../../components/pesquisa-elenco/pesquisa-elenco.component";
import Erro from "../../components/erro/erro.component";
import { useSelector } from "react-redux";
import TirarFoto from "../../components/tirar-foto/tirar-foto.component";

export function HomeScreen() {
  const navigate = useNavigate();
  const { mudanca, input } = useNomeAtor();
  const { changeSerie, serieNome } = useNomeSerie();
  const [tab, setTab] = useState("FOTO");
  const [tipo, setTipo] = useState("TV");
  const { hasError } = useSelector((state) => state.error);

  useEffect(() => {}, [hasError]);

  const handleSubmit = (event) => {
    event.preventDefault();
    navigate(`/exibirProducoes/${encodeURIComponent(input)}`);
  };

  const handleSubmitElenco = (event) => {
    event.preventDefault();
    navigate(`/exibirElenco/${encodeURIComponent(serieNome.trim())}/${tipo}`);
  };

  return (
    <div className="container">
      {hasError ? <Erro /> : <></>}
      <Logo />
      {tab === "PESSOA" ? (
        <PesquisaAtor handleSubmit={handleSubmit} onChange={mudanca} />
      ) : tab === "ELENCO" ? (
        <div className="pesquisa-elenco">
          <PesquisaElenco
            handleSubmit={handleSubmitElenco}
            onChange={changeSerie}
            tab={tipo}
          />
          <Tab
            setTab={setTipo}
            estado={tipo}
            tabs={["TV", "MOVIE"]}
            tamanho="pequeno"
          />
        </div>
      ) : (
        <TirarFoto />
      )}
      <div className="menu">
        <Tab
          setTab={setTab}
          estado={tab}
          tabs={["FOTO", "PESSOA", "ELENCO"]}
          tamanho="normal"
        />
      </div>
    </div>
  );
}

export default HomeScreen;
