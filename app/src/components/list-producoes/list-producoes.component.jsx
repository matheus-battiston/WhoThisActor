import "./list-producoes.css";
import ExibeProducao from "../exibe-producao/exibe-producao.component";

const ListProducoes = ({ producoes, handleSwipe, tab }) => {
  const producoesFiltradas =
    tab === "FILME"
      ? producoes.producoes?.filmes || []
      : producoes.producoes?.series || [];

  return (
    <div {...handleSwipe} className="producoes-list">
      {producoesFiltradas.map((item) => (
        <ExibeProducao
          key={`${tab}-${item.id}-${item.nomePersonagem}`}
          producao={item}
          tipo={tab}
        />
      ))}
    </div>
  );
};
export default ListProducoes;
