import "./list-producoes.css";
import ExibeProducao from "../exibe-producao/exibe-producao.component";

const ListProducoes = ({ producoes, handleSwipe, tab }) => {
  const filmes = producoes.producoes.filter(
    (item) => item.tipoProducao === "FILME"
  );
  const series = producoes.producoes.filter(
    (item) => item.tipoProducao === "TV"
  );

  const producoesFiltradas = tab === "FILME" ? filmes : series;

  return (
    <div {...handleSwipe} className="producoes-list">
      {producoesFiltradas.map((item, index) => (
        <ExibeProducao key={index} producao={item} tipo={tab} />
      ))}
    </div>
  );
};

export default ListProducoes;
