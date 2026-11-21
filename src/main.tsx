import { createRoot } from 'react-dom/client';
import { registerSW } from 'virtual:pwa-register';
import App from './App.tsx';
import './index.css';

try {
  registerSW({ immediate: true });
} catch {
  // Ignore SW registration failure in restricted preview contexts
}

createRoot(document.getElementById('root')!).render(<App />);
