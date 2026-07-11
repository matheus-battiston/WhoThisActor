import {
  URL_BASE_BACKDROP_TMDB,
  URL_BASE_IMAGEM_TMDB,
} from "../../constants/image-tmdb";
import { obterAnoLancamento } from "../../utils/formatar-data-lancamento";

export function montarUrlImagem(path, base = URL_BASE_IMAGEM_TMDB) {
  return path ? `${base}${path}` : undefined;
}

export function montarUrlBackdrop(path) {
  return montarUrlImagem(path, URL_BASE_BACKDROP_TMDB);
}

export function montarPeriodo(producao) {
  if (producao.tipoMidia === "MOVIE") {
    return obterAnoLancamento(producao.dataLancamento);
  }

  const { anoPrimeiraTemporada, anoUltimaTemporada } = producao;

  if (anoPrimeiraTemporada && anoUltimaTemporada) {
    return `${anoPrimeiraTemporada} - ${anoUltimaTemporada}`;
  }

  if (anoPrimeiraTemporada) {
    return `${anoPrimeiraTemporada} - Atual`;
  }

  return null;
}

export function montarTemporadas(quantidadeTemporadas) {
  if (!quantidadeTemporadas) return null;

  const sufixo = quantidadeTemporadas === 1 ? "temporada" : "temporadas";
  return `${quantidadeTemporadas} ${sufixo}`;
}
