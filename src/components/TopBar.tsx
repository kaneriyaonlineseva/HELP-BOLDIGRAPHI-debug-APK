import React, { useState } from 'react';
import { Download, Moon, Sun, WifiOff } from 'lucide-react';
import { AppTab, ThemeMode } from '../types/typography';
import { usePWAInstall } from '../hooks/usePWAInstall';
import { useOnlineStatus } from '../hooks/useOnlineStatus';

interface TopBarProps {
  activeTab: AppTab;
  onTabChange: (tab: AppTab) => void;
  theme: ThemeMode;
  onToggleTheme: () => void;
  favoritesCount: number;
  historyCount: number;
}

export const TopBar: React.FC<TopBarProps> = ({
  activeTab,
  onTabChange,
  theme,
  onToggleTheme,
  favoritesCount,
  historyCount,
}) => {
  const { isInstallable, isInstalled, isIOS, install } = usePWAInstall();
  const isOnline = useOnlineStatus();
  const [showIosGuide, setShowIosGuide] = useState(false);

  const navItems: { id: AppTab; label: string; count?: number }[] = [
    { id: 'studio', label: 'Studio' },
    { id: 'favorites', label: 'Favorites', count: favoritesCount },
    { id: 'history', label: 'History', count: historyCount },
    { id: 'android', label: 'Android APK' },
  ];

  return (
    <>
      <header
        className="sticky top-0 z-30 h-14 border-b backdrop-blur-md transition-colors"
        style={{
          backgroundColor:
            theme === 'dark' ? 'rgba(11, 11, 18, 0.88)' : 'rgba(246, 245, 250, 0.90)',
          borderColor: 'var(--border-subtle)',
        }}
      >
        <div className="mx-auto flex h-full max-w-[1200px] items-center justify-between gap-4 px-4 sm:px-6 lg:px-8">
          {/* Zone 1: Single-line Brand Wordmark */}
          <button
            type="button"
            onClick={() => onTabChange('studio')}
            className="font-syne whitespace-nowrap shrink-0 text-base sm:text-lg font-extrabold tracking-tight focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-violet-500 rounded-md px-1"
            style={{ color: 'var(--text-primary)' }}
          >
            HELP <span style={{ color: 'var(--accent-violet)' }}>BOLDIGRAPHI</span>
          </button>

          {/* Zone 2: Desktop Single-line Navigation Links */}
          <nav className="hidden md:flex items-center gap-6" aria-label="Main navigation">
            {navItems.map((item) => {
              const isActive = activeTab === item.id;
              return (
                <button
                  key={item.id}
                  type="button"
                  onClick={() => onTabChange(item.id)}
                  className={`relative py-1.5 text-sm font-medium whitespace-nowrap shrink-0 transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-violet-500 rounded-md px-1 ${
                    isActive
                      ? 'text-[var(--text-primary)]'
                      : 'text-[var(--text-secondary)] hover:text-[var(--text-primary)]'
                  }`}
                >
                  <span>{item.label}</span>
                  {typeof item.count === 'number' && item.count > 0 && (
                    <span
                      className="ml-1.5 font-grotesk text-xs tabular-nums"
                      style={{ color: isActive ? 'var(--accent-gold)' : 'var(--text-muted)' }}
                    >
                      {item.count}
                    </span>
                  )}
                  {isActive && (
                    <span
                      className="absolute inset-x-1 -bottom-[15px] h-0.5 rounded-full"
                      style={{ backgroundColor: 'var(--accent-violet)' }}
                    />
                  )}
                </button>
              );
            })}
          </nav>

          {/* Zone 3: Theme & Install Action */}
          <div className="flex items-center gap-2 shrink-0">
            {!isOnline && (
              <span
                className="hidden sm:inline-flex items-center gap-1.5 text-xs font-grotesk whitespace-nowrap"
                style={{ color: 'var(--accent-gold)' }}
                title="Working offline using cached assets and local storage"
              >
                <WifiOff className="h-3.5 w-3.5" aria-hidden="true" />
                Offline
              </span>
            )}

            {!isInstalled && isInstallable && (
              <button
                type="button"
                onClick={install}
                className="inline-flex min-h-[38px] items-center gap-1.5 rounded-full px-3.5 py-1.5 text-xs font-grotesk font-semibold text-white whitespace-nowrap shrink-0 transition-transform active:scale-95 focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-violet-400"
                style={{
                  background: 'linear-gradient(135deg, #8B5CF6 0%, #7C3AED 100%)',
                }}
              >
                <Download className="h-3.5 w-3.5" aria-hidden="true" />
                <span>Install</span>
              </button>
            )}

            {!isInstalled && !isInstallable && isIOS && (
              <button
                type="button"
                onClick={() => setShowIosGuide(true)}
                className="inline-flex min-h-[38px] items-center gap-1.5 rounded-full border px-3 py-1.5 text-xs font-grotesk font-semibold whitespace-nowrap shrink-0 transition-colors"
                style={{
                  borderColor: 'var(--border-subtle)',
                  backgroundColor: 'var(--surface-card)',
                  color: 'var(--text-primary)',
                }}
              >
                <Download className="h-3.5 w-3.5" aria-hidden="true" />
                <span>Install</span>
              </button>
            )}

            <button
              type="button"
              onClick={onToggleTheme}
              aria-label={theme === 'dark' ? 'Switch to Light Mode' : 'Switch to Dark Mode'}
              className="inline-flex min-h-[40px] min-w-[40px] items-center justify-center gap-1.5 rounded-full border px-3 py-1.5 text-xs font-grotesk font-semibold whitespace-nowrap shrink-0 transition-colors hover:border-violet-500 focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-violet-500"
              style={{
                backgroundColor: 'var(--surface-card)',
                borderColor: 'var(--border-subtle)',
                color: 'var(--text-primary)',
              }}
            >
              {theme === 'dark' ? (
                <>
                  <Sun className="h-4 w-4 text-amber-400" aria-hidden="true" />
                  <span className="hidden sm:inline">Light</span>
                </>
              ) : (
                <>
                  <Moon className="h-4 w-4 text-violet-700" aria-hidden="true" />
                  <span className="hidden sm:inline">Dark</span>
                </>
              )}
            </button>
          </div>
        </div>
      </header>

      {showIosGuide && (
        <div
          className="fixed inset-0 z-50 flex items-center justify-center bg-black/65 p-4 backdrop-blur-sm"
          role="dialog"
          aria-modal="true"
          aria-labelledby="ios-install-title"
        >
          <div
            className="w-full max-w-sm rounded-3xl border p-6"
            style={{
              backgroundColor: 'var(--surface-elevated)',
              borderColor: 'var(--border-subtle)',
              color: 'var(--text-primary)',
            }}
          >
            <h3 id="ios-install-title" className="font-syne text-lg font-bold">
              Install HELP BOLDIGRAPHI
            </h3>
            <p className="mt-2 text-sm leading-relaxed" style={{ color: 'var(--text-secondary)' }}>
              1. Tap the <strong>Share</strong> icon in your Safari toolbar.
              <br />
              2. Scroll down and select <strong>Add to Home Screen</strong> for full offline access.
            </p>
            <button
              type="button"
              onClick={() => setShowIosGuide(false)}
              className="mt-5 w-full min-h-[44px] rounded-full text-sm font-grotesk font-semibold text-white"
              style={{ background: 'linear-gradient(135deg, #8B5CF6 0%, #7C3AED 100%)' }}
            >
              Got it
            </button>
          </div>
        </div>
      )}
    </>
  );
};
