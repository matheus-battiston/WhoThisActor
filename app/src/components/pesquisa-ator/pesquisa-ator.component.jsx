import "./pesquisa-ator.css";
import CameraComponent from "../../components/camera/camera.component";
import InputSearch from "../../components/pesquisa/pesquisa.component";
import { Switch } from "@mui/material";
import FormGroup from "@mui/material/FormGroup";
import FormControlLabel from "@mui/material/FormControlLabel";
import { useState } from "react";

const PesquisaAtor = ({ handleSubmit, onChange }) => {
  const [checked, setChecked] = useState(false);

  const handleChangeFast = (event) => {
    setChecked(event.target.checked);
  };
  return (
    <div className="container-pesquisa-ator">
      <form onSubmit={handleSubmit}>
        <InputSearch
          onChange={onChange}
          texto={"Já sabe o nome? Pesquise aqui:"}
          placeholder={"Adam Sandler"}
        />
      </form>
      <CameraComponent checked={checked} />
      <FormGroup className="slider">
        <FormControlLabel
          control={<Switch checked={checked} onChange={handleChangeFast} />}
          label="Detecção rapida"
        />
      </FormGroup>
    </div>
  );
};

export default PesquisaAtor;
