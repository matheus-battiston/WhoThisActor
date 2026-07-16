import { useEffect, useState } from "react";

const MOBILE_QUERY = "(max-width: 767px)";

function verificarMediaQuery(query) {
  if (typeof window === "undefined") return false;

  return window.matchMedia(query).matches;
}

export function useIsMobile(query = MOBILE_QUERY) {
  const [isMobile, setIsMobile] = useState(() => verificarMediaQuery(query));

  useEffect(() => {
    if (typeof window === "undefined") return undefined;

    const mediaQuery = window.matchMedia(query);
    const atualizar = () => setIsMobile(mediaQuery.matches);

    atualizar();

    if (mediaQuery.addEventListener) {
      mediaQuery.addEventListener("change", atualizar);
    } else {
      mediaQuery.addListener(atualizar);
    }

    return () => {
      if (mediaQuery.removeEventListener) {
        mediaQuery.removeEventListener("change", atualizar);
      } else {
        mediaQuery.removeListener(atualizar);
      }
    };
  }, [query]);

  return isMobile;
}
