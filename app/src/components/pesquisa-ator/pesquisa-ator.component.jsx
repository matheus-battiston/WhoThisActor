import "./pesquisa-ator.css";
import InputSearch from "../../components/pesquisa/pesquisa.component";

const PesquisaAtor = ({ handleSubmit, onChange }) => {
  return (
    <div className="container-pesquisa-ator">
      <form onSubmit={handleSubmit}>
        <InputSearch
          onChange={onChange}
          texto={"Já sabe o nome? Pesquise aqui:"}
          placeholder={"Adam Sandler"}
        />
      </form>
    </div>
  );
};

export default PesquisaAtor;
