import InputSearch from "../pesquisa/pesquisa.component";
import "./pesquisa-elenco.css";

const PesquisaElenco = ({ handleSubmit, onChange, tab }) => {
  const PLACEHOLDER = tab === "TV" ? "Modern Family" : "Just Go With It";
  return (
    <div>
      <form onSubmit={handleSubmit}>
        <InputSearch
          onChange={onChange}
          texto={"Digite o nome"}
          placeholder={PLACEHOLDER}
        />
      </form>
    </div>
  );
};

export default PesquisaElenco;
