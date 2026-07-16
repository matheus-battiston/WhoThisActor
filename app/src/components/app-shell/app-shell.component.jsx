import { Outlet } from "react-router-dom";
import { CameraCaptureProvider } from "../camera-capture/camera-capture.context";
import MobileFooterMenu from "../mobile-footer-menu/mobile-footer-menu.component";
import WebSideMenu from "../web-side-menu/web-side-menu.component";
import "./app-shell.css";

export default function AppShell() {
  return (
    <CameraCaptureProvider>
      <div className="app-shell">
        <WebSideMenu />
        <div className="app-shell-content">
          <Outlet />
        </div>
      </div>
      <MobileFooterMenu />
    </CameraCaptureProvider>
  );
}
