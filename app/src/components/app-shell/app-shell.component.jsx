import { Outlet } from "react-router-dom";
import { CameraCaptureProvider } from "../camera-capture/camera-capture.context";
import MobileFooterMenu from "../mobile-footer-menu/mobile-footer-menu.component";

export default function AppShell() {
  return (
    <CameraCaptureProvider>
      <Outlet />
      <MobileFooterMenu />
    </CameraCaptureProvider>
  );
}
