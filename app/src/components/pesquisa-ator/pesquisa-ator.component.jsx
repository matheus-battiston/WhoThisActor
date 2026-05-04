import "./pesquisa-ator.css";
import InputSearch from "../../components/pesquisa/pesquisa.component";

const PesquisaAtor = ({ handleSubmit, onChange }) => {
  return (
    <div>
      <form onSubmit={handleSubmit}>
        <InputSearch onChange={onChange} placeholder={"Adam Sandler"} />
      </form>
    </div>
  );
};

export default PesquisaAtor;
