import { useIsMobile } from "../../hooks/use-is-mobile/use-is-mobile.hook";
import SobreMobileLayout from "./layouts/sobre-mobile.layout";
import SobreWebLayout from "./layouts/sobre-web.layout";

export function SobreScreen() {
  const isMobile = useIsMobile();

  return isMobile ? <SobreMobileLayout /> : <SobreWebLayout />;
}
