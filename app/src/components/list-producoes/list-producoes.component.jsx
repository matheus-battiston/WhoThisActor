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

  console.log(producoesFiltradas);

  return (
    <div {...handleSwipe} className="producoesList">
      {producoesFiltradas.map((item, index) => (
        <ExibeProducao key={index} producao={item} />
      ))}
    </div>
  );
};

export default ListProducoes;
