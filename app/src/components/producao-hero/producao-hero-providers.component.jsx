import { montarUrlImagem } from "./producao-hero.utils";

export default function ProducaoHeroProviders({ providers, className }) {
  if (!providers?.length) return null;

  return (
    <div className={className}>
      <div>
        {providers.map((provider) => (
          <img
            key={provider.provider_id || provider.provider_name}
            src={montarUrlImagem(provider.logo_path)}
            alt={provider.provider_name}
          />
        ))}
      </div>
    </div>
  );
}
