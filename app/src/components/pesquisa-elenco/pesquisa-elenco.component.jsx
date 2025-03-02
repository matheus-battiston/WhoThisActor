import InputSearch from "../pesquisa/pesquisa.component";
import "./pesquisa-elenco.css";

const PesquisaElenco = ({ handleSubmit, onChange }) => {
  return (
    <div>
      <form onSubmit={handleSubmit}>
        <InputSearch
          onChange={onChange}
          texto={"Digite o nome"}
          placeholder={"Breaking Bad"}
        />
      </form>
    </div>
  );
};

export default PesquisaElenco;
