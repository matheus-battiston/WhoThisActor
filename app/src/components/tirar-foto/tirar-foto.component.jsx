import CameraComponent from "../camera/camera.component";
import InputSearch from "../pesquisa/pesquisa.component";
import "./tirar-foto.css";
import { useState } from "react";
import Tab from "../../components/tab/tab.component";
import { useNomeSerie } from "../../api/hooks/useNomeSerie/useNomeSerie.hook";

const TirarFoto = () => {
  const [tipo, setTipo] = useState("TV");
  const { changeSerie, serieNome } = useNomeSerie();

  return (
    <div className="tirar-foto-container">
      <div>
        <InputSearch
          onChange={changeSerie}
          texto={"O que você está assistindo?:"}
          placeholder={"Modern Family"}
        />
        <Tab
          setTab={setTipo}
          estado={tipo}
          tabs={["TV", "MOVIE"]}
          tamanho="pequeno"
        />
      </div>
      <CameraComponent tipo={tipo} serieNome={serieNome} />
    </div>
  );
};

export default TirarFoto;
