export function obterAnoLancamento(dataLancamento) {
  if (!dataLancamento) return null;

  return String(dataLancamento).slice(0, 4);
}
