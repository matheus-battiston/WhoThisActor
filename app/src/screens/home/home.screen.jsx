import React, { useState } from "react";
import Logo from "../../components/logo/logo.component";
import { useNavigate } from "react-router-dom"; // Importando useNavigate
import "./home-screen.css";
import { useNomeAtor } from "../../api/hooks/useNomeAtor/useNomeAtor.hook";
import { useNomeSerie } from "../../api/hooks/useNomeSerie/useNomeSerie.hook";
import PesquisaAtor from "../../components/pesquisa-ator/pesquisa-ator.component";
import Tab from "../../components/tab/tab.component";
import PesquisaElenco from "../../components/pesquisa-elenco/pesquisa-elenco.component";

export function HomeScreen() {
  const navigate = useNavigate(); // Inicializando useNavigate
  const { mudanca, input } = useNomeAtor();
  const { changeSerie, serieNome } = useNomeSerie();
  const [tab, setTab] = useState("PESSOA");
  const [tipo, setTipo] = useState("TV");

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
      <Logo />
      {tab === "PESSOA" ? (
        <PesquisaAtor handleSubmit={handleSubmit} onChange={mudanca} />
      ) : (
        <div className="pesquisa-elenco">
          <PesquisaElenco
            handleSubmit={handleSubmitElenco}
            onChange={changeSerie}
          />
          <Tab
            setTab={setTipo}
            estado={tipo}
            tabs={["TV", "MOVIE"]}
            tamanho="pequeno"
          />
        </div>
      )}
      <div className="menu">
        <Tab
          setTab={setTab}
          estado={tab}
          tabs={["PESSOA", "ELENCO"]}
          tamanho="normal"
        />
      </div>
    </div>
  );
}

export default HomeScreen;
