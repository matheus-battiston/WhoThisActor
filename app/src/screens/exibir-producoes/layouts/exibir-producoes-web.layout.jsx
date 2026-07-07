import { useSwipeable } from "react-swipeable";
import AtorInfo from "../../../components/ator-info/ator-info.component";
import Cabecalho from "../../../components/cabecalho/cabecalho.component";
import ListProducoes from "../../../components/list-producoes/list-producoes.component";
import Tab from "../../../components/tab/tab.component";
import "./exibir-producoes-web.css";

export default function ExibirProducoesWebLayout({
  ator,
  tab,
  setTab,
  isAuthenticated,
  favoritado,
  favoritoPendente,
  favoritar,
}) {
  const handleSwipe = useSwipeable({
    onSwipedLeft: () => setTab("TV"),
    onSwipedRight: () => setTab("FILME"),
    trackMouse: true,
  });

  return (
    <div className="exibir-producoes-web">
      <Cabecalho />

      <div className="exibir-producoes-web-ator-info">
        <AtorInfo
          nome={ator.nome}
          imagem={ator.urlFoto}
          logado={isAuthenticated}
          favoritado={favoritado}
          favoritar={favoritar}
          favoritoPendente={favoritoPendente}
        />
      </div>

      <Tab setTab={setTab} estado={tab} tabs={["FILME", "TV"]} />

      <ListProducoes producoes={ator} handleSwipe={handleSwipe} tab={tab} />
    </div>
  );
}
