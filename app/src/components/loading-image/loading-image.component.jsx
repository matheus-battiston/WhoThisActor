import { useEffect, useState } from "react";
import "./loading-image.css";
import defaultFallbackPoster from "../../assets/images/poster-placeholder.svg";

export function LoadingImage({
  src,
  fallbackSrc = defaultFallbackPoster,
  alt,
  className = "",
  imgClassName = "",
}) {
  const [currentSrc, setCurrentSrc] = useState(src ?? fallbackSrc);
  const [imagemCarregada, setImagemCarregada] = useState(false);

  useEffect(() => {
    let ativo = true;
    const imagemInicial = src ?? fallbackSrc;

    setCurrentSrc(imagemInicial);
    setImagemCarregada(false);

    if (!imagemInicial) {
      setImagemCarregada(true);
      return undefined;
    }

    function carregarImagem(imagemSrc, usarFallback) {
      const img = new Image();

      img.onload = () => {
        if (!ativo) return;
        setCurrentSrc(imagemSrc);
        setImagemCarregada(true);
      };

      img.onerror = () => {
        if (!ativo) return;

        if (usarFallback && fallbackSrc && imagemSrc !== fallbackSrc) {
          carregarImagem(fallbackSrc, false);
          return;
        }

        setCurrentSrc(imagemSrc);
        setImagemCarregada(true);
      };

      img.src = imagemSrc;
    }

    carregarImagem(imagemInicial, true);

    return () => {
      ativo = false;
    };
  }, [src, fallbackSrc]);

  return (
    <div className={`loading-image-wrapper ${className}`.trim()}>
      {!imagemCarregada && <div className="loading-image-skeleton" />}
      <img
        src={currentSrc}
        alt={alt}
        className={`loading-image ${imgClassName} ${imagemCarregada ? "loading-image-loaded" : "loading-image-hidden"}`.trim()}
      />
    </div>
  );
}
