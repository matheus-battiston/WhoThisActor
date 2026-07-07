import { useParams } from "react-router-dom";
import { useNomePersonagem } from "../../hooks/use-nome-personagem/use-nome-personagem.hook";
import Loading from "../../components/loading/loading.component";
import { useAuth } from "../../hooks/use-auth/use-auth.hook";
import { useIsMobile } from "../../hooks/use-is-mobile/use-is-mobile.hook";
import { useFavoritoProducao } from "../../api/hooks/use-favorito-producao/use-favorito-producao.hook";
import {
  usePesquisarDetalhesFilmeComElenco,
  usePesquisarDetalhesSerieComElenco,
} from "../../api/generated/api";
import ExibirElencoMobileLayout from "./layouts/exibir-elenco-mobile.layout";
import ExibirElencoWebLayout from "./layouts/exibir-elenco-web.layout";
import "./exibir-elenco.css";

const FRASE_CARREGAMENTO = "Carregando elenco";
const PLACEHOLDER_PERSONAGEM = "Alex Dunphy";

export function ExibirElencoScreen() {
  const { changePersonagem, personagemNome } = useNomePersonagem();
  const { tipo, id } = useParams();
  const isMobile = useIsMobile();

  const idProducao = Number(id);
  const isFilme = tipo === "MOVIE";

  const { isAuthenticated, authChecked } = useAuth();

  const parametrosPesquisa = {
    personagem: personagemNome || undefined,
  };

  const detalhesSerie = usePesquisarDetalhesSerieComElenco(
    idProducao,
    parametrosPesquisa,
    {
      query: {
        enabled: !isFilme && Boolean(idProducao),
        placeholderData: (previousData) => previousData,
      },
    },
  );

  const detalhesFilme = usePesquisarDetalhesFilmeComElenco(
    idProducao,
    parametrosPesquisa,
    {
      query: {
        enabled: isFilme && Boolean(idProducao),
        placeholderData: (previousData) => previousData,
      },
    },
  );

  const detalhesQuery = isFilme ? detalhesFilme : detalhesSerie;

  const detalhesProducao = detalhesQuery.data?.data;

  const { favoritoLocal, favoritoPendente, alternarFavorito } =
    useFavoritoProducao(
      detalhesProducao?.id,
      tipo,
      isAuthenticated,
      detalhesProducao?.estaFavoritado,
    );

  const carregamentoInicial =
    !authChecked || detalhesQuery.isLoading || detalhesProducao == null;
  const elenco = detalhesProducao?.elenco ?? [];

  const layoutProps = {
    detalhesProducao,
    elenco,
    onPesquisarPersonagem: changePersonagem,
    isAuthenticated,
    favoritado: favoritoLocal,
    favoritoPendente,
    favoritar: alternarFavorito,
  };

  return (
    <div className="exibir-elenco-screen">
      {carregamentoInicial ? (
        <Loading frase={FRASE_CARREGAMENTO} />
      ) : isMobile ? (
        <ExibirElencoMobileLayout {...layoutProps} />
      ) : (
        <ExibirElencoWebLayout
          {...layoutProps}
          placeholderPesquisa={PLACEHOLDER_PERSONAGEM}
        />
      )}
    </div>
  );
}
