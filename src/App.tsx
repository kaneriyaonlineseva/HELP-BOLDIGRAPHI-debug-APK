import { useEffect, useMemo, useState } from 'react';
import { RotateCcw, WifiOff } from 'lucide-react';
import { STYLE_CATALOG } from './data/styleCatalog';
import {
  appendHistoryItem,
  loadDraftText,
  loadFavorites,
  loadHistory,
  loadTheme,
  saveDraftText,
  saveFavorites,
  saveHistory,
  saveTheme,
} from './services/storageService';
import { copyToClipboard } from './services/shareService';
import {
  AppTab,
  SavedTextItem,
  ScriptFilter,
  StyleCategory,
  StylePreset,
  ThemeMode,
} from './types/typography';
import { TopBar } from './components/TopBar';
import { MasterEditor } from './components/MasterEditor';
import { FilterToolbar } from './components/FilterToolbar';
import { StyleCard } from './components/StyleCard';
import { PngStudioModal } from './components/PngStudioModal';
import { ShareSheetModal } from './components/ShareSheetModal';
import { FavoritesView } from './components/FavoritesView';
import { HistoryView } from './components/HistoryView';
import { AndroidGuideView } from './components/AndroidGuideView';
import { MobileBottomNav } from './components/MobileBottomNav';
import { useOnlineStatus } from './hooks/useOnlineStatus';

export default function App() {
  const [theme, setTheme] = useState<ThemeMode>(() => loadTheme());
  const [activeTab, setActiveTab] = useState<AppTab>('studio');
  const [inputText, setInputText] = useState<string>(() => loadDraftText());
  const [scriptFilter, setScriptFilter] = useState<ScriptFilter>('all');
  const [selectedCategory, setSelectedCategory] = useState<StyleCategory>('all');
  const [searchQuery, setSearchQuery] = useState('');
  const [previewFontSize, setPreviewFontSize] = useState(22);

  const [favorites, setFavorites] = useState<SavedTextItem[]>(() => loadFavorites());
  const [history, setHistory] = useState<SavedTextItem[]>(() => loadHistory());

  const [copiedId, setCopiedId] = useState<string | null>(null);
  const [toastMessage, setToastMessage] = useState<string | null>(null);

  const [shareTarget, setShareTarget] = useState<{
    text: string;
    styleId: string;
    styleName: string;
    script: 'english' | 'hindi' | 'both';
    fontClass?: string;
    canvasFontFamily: string;
  } | null>(null);

  const [pngTarget, setPngTarget] = useState<{
    text: string;
    styleId: string;
    styleName: string;
    canvasFontFamily: string;
  } | null>(null);

  const isOnline = useOnlineStatus();

  // Sync theme class & meta theme-color
  useEffect(() => {
    const root = document.documentElement;
    if (theme === 'dark') {
      root.classList.add('dark');
    } else {
      root.classList.remove('dark');
    }
    saveTheme(theme);

    const metaTheme = document.querySelector('meta[name="theme-color"]');
    if (metaTheme) {
      metaTheme.setAttribute('content', theme === 'dark' ? '#0B0B12' : '#F6F5FA');
    }
  }, [theme]);

  // Persist draft text locally
  useEffect(() => {
    saveDraftText(inputText);
  }, [inputText]);

  const showToast = (msg: string) => {
    setToastMessage(msg);
  };

  useEffect(() => {
    if (!toastMessage) return;
    const timer = window.setTimeout(() => setToastMessage(null), 2600);
    return () => window.clearTimeout(timer);
  }, [toastMessage]);

  const effectiveText = inputText.trim() ? inputText : 'HELP BOLDIGRAPHI\nआपका स्वागत है';

  const filteredPresets = useMemo(() => {
    const q = searchQuery.trim().toLowerCase();
    return STYLE_CATALOG.filter((preset) => {
      if (scriptFilter !== 'all' && preset.script !== 'both' && preset.script !== scriptFilter) {
        return false;
      }
      if (selectedCategory !== 'all' && preset.category !== selectedCategory) {
        return false;
      }
      if (q) {
        const hay = `${preset.name} ${preset.subtitle} ${preset.category} ${preset.script}`.toLowerCase();
        if (!hay.includes(q)) return false;
      }
      return true;
    });
  }, [scriptFilter, selectedCategory, searchQuery]);

  const handleCopyPreset = async (preset: StylePreset, renderedText: string) => {
    const ok = await copyToClipboard(renderedText);
    if (!ok) {
      showToast('Clipboard permission denied. Select and copy manually.');
      return;
    }
    setCopiedId(preset.id);
    window.setTimeout(() => {
      setCopiedId((prev) => (prev === preset.id ? null : prev));
    }, 1800);

    setHistory((prev) =>
      appendHistoryItem(prev, {
        sourceText: effectiveText,
        styledText: renderedText,
        styleId: preset.id,
        styleName: preset.name,
        script: preset.script,
        fontClass: preset.fontClass,
        canvasFontFamily: preset.canvasFontFamily,
        actionType: 'copied',
      })
    );
    showToast(`Copied "${preset.name}" to clipboard.`);
  };

  const handleCopySavedItem = async (item: SavedTextItem) => {
    const ok = await copyToClipboard(item.styledText);
    if (!ok) {
      showToast('Clipboard copy failed.');
      return;
    }
    setCopiedId(item.id);
    window.setTimeout(() => {
      setCopiedId((prev) => (prev === item.id ? null : prev));
    }, 1800);
    showToast(`Copied "${item.styleName}" to clipboard.`);
  };

  const isPresetFavorited = (presetId: string, renderedText: string) =>
    favorites.some((f) => f.styleId === presetId && f.styledText === renderedText);

  const handleToggleFavorite = (preset: StylePreset, renderedText: string) => {
    const existing = favorites.find(
      (f) => f.styleId === preset.id && f.styledText === renderedText
    );
    if (existing) {
      const next = favorites.filter((f) => f.id !== existing.id);
      setFavorites(next);
      saveFavorites(next);
      showToast(`Removed "${preset.name}" from Favorites.`);
    } else {
      const nextItem: SavedTextItem = {
        id: `${Date.now()}-${Math.random().toString(36).slice(2, 8)}`,
        sourceText: effectiveText,
        styledText: renderedText,
        styleId: preset.id,
        styleName: preset.name,
        script: preset.script,
        fontClass: preset.fontClass,
        canvasFontFamily: preset.canvasFontFamily,
        createdAt: Date.now(),
        actionType: 'favorited',
      };
      const next = [nextItem, ...favorites];
      setFavorites(next);
      saveFavorites(next);
      showToast(`Saved "${preset.name}" to Favorites.`);
    }
  };

  const handleRemoveFavorite = (id: string) => {
    const next = favorites.filter((f) => f.id !== id);
    setFavorites(next);
    saveFavorites(next);
    showToast('Removed from Favorites.');
  };

  const handleClearFavorites = () => {
    setFavorites([]);
    saveFavorites([]);
    showToast('Cleared all Favorites.');
  };

  const handleRemoveHistory = (id: string) => {
    const next = history.filter((h) => h.id !== id);
    setHistory(next);
    saveHistory(next);
  };

  const handleClearHistory = () => {
    setHistory([]);
    saveHistory([]);
    showToast('History cleared.');
  };

  return (
    <div className="min-h-screen flex flex-col pb-16 md:pb-8">
      <TopBar
        activeTab={activeTab}
        onTabChange={setActiveTab}
        theme={theme}
        onToggleTheme={() => setTheme((prev) => (prev === 'dark' ? 'light' : 'dark'))}
        favoritesCount={favorites.length}
        historyCount={history.length}
      />

      {!isOnline && (
        <div
          className="border-b px-4 py-2 text-center text-xs font-grotesk"
          style={{
            backgroundColor: 'var(--surface-low)',
            borderColor: 'var(--border-subtle)',
            color: 'var(--accent-gold)',
          }}
        >
          <span className="inline-flex items-center gap-1.5">
            <WifiOff className="h-3.5 w-3.5" aria-hidden="true" />
            Offline Mode — Text generation, PNG export, Favorites, and History remain available.
          </span>
        </div>
      )}

      <main className="mx-auto w-full max-w-[1200px] flex-1 px-4 py-5 sm:px-6 lg:px-8">
        {activeTab === 'studio' && (
          <div className="grid grid-cols-1 md:grid-cols-12 gap-6 items-start">
            {/* Left Pane: Sticky Master Editor on Tablet/Desktop */}
            <div className="md:col-span-5 lg:col-span-4 md:sticky md:top-20 space-y-4">
              <MasterEditor
                inputText={inputText}
                onInputChange={setInputText}
                scriptFilter={scriptFilter}
                onScriptFilterChange={setScriptFilter}
                previewFontSize={previewFontSize}
                onPreviewFontSizeChange={setPreviewFontSize}
              />
            </div>

            {/* Right Pane: Search, Category Filters & Live Styled Cards */}
            <div className="md:col-span-7 lg:col-span-8 space-y-4">
              <FilterToolbar
                searchQuery={searchQuery}
                onSearchChange={setSearchQuery}
                selectedCategory={selectedCategory}
                onCategoryChange={setSelectedCategory}
                resultCount={filteredPresets.length}
              />

              {filteredPresets.length === 0 ? (
                <div
                  className="rounded-3xl border p-10 text-center"
                  style={{
                    backgroundColor: 'var(--surface-card)',
                    borderColor: 'var(--border-subtle)',
                  }}
                >
                  <p className="font-syne text-lg font-bold" style={{ color: 'var(--text-primary)' }}>
                    No Matching Styles Found
                  </p>
                  <p className="mt-1.5 text-sm" style={{ color: 'var(--text-secondary)' }}>
                    Try clearing your search filter or switching script to All.
                  </p>
                  <button
                    type="button"
                    onClick={() => {
                      setSearchQuery('');
                      setSelectedCategory('all');
                      setScriptFilter('all');
                    }}
                    className="mt-4 inline-flex min-h-[42px] items-center gap-2 rounded-full px-5 py-2 text-xs font-grotesk font-semibold text-white"
                    style={{ background: 'linear-gradient(135deg, #8B5CF6 0%, #7C3AED 100%)' }}
                  >
                    <RotateCcw className="h-3.5 w-3.5" aria-hidden="true" />
                    Reset Filters
                  </button>
                </div>
              ) : (
                <div className="grid grid-cols-1 lg:grid-cols-2 gap-4">
                  {filteredPresets.map((preset) => {
                    const renderedText = preset.transform(effectiveText);
                    return (
                      <StyleCard
                        key={preset.id}
                        preset={preset}
                        renderedText={renderedText}
                        previewFontSize={previewFontSize}
                        isCopied={copiedId === preset.id}
                        isFavorited={isPresetFavorited(preset.id, renderedText)}
                        onCopy={() => handleCopyPreset(preset, renderedText)}
                        onShare={() =>
                          setShareTarget({
                            text: renderedText,
                            styleId: preset.id,
                            styleName: preset.name,
                            script: preset.script,
                            fontClass: preset.fontClass,
                            canvasFontFamily: preset.canvasFontFamily,
                          })
                        }
                        onExportPng={() =>
                          setPngTarget({
                            text: renderedText,
                            styleId: preset.id,
                            styleName: preset.name,
                            canvasFontFamily: preset.canvasFontFamily,
                          })
                        }
                        onToggleFavorite={() => handleToggleFavorite(preset, renderedText)}
                      />
                    );
                  })}
                </div>
              )}
            </div>
          </div>
        )}

        {activeTab === 'favorites' && (
          <FavoritesView
            favorites={favorites}
            copiedId={copiedId}
            onCopy={handleCopySavedItem}
            onShare={(item) =>
              setShareTarget({
                text: item.styledText,
                styleId: item.styleId,
                styleName: item.styleName,
                script: item.script,
                fontClass: item.fontClass,
                canvasFontFamily: item.canvasFontFamily,
              })
            }
            onExportPng={(item) =>
              setPngTarget({
                text: item.styledText,
                styleId: item.styleId,
                styleName: item.styleName,
                canvasFontFamily: item.canvasFontFamily,
              })
            }
            onRemove={handleRemoveFavorite}
            onClearAll={handleClearFavorites}
            onGoToStudio={() => setActiveTab('studio')}
          />
        )}

        {activeTab === 'history' && (
          <HistoryView
            history={history}
            copiedId={copiedId}
            onCopy={handleCopySavedItem}
            onShare={(item) =>
              setShareTarget({
                text: item.styledText,
                styleId: item.styleId,
                styleName: item.styleName,
                script: item.script,
                fontClass: item.fontClass,
                canvasFontFamily: item.canvasFontFamily,
              })
            }
            onExportPng={(item) =>
              setPngTarget({
                text: item.styledText,
                styleId: item.styleId,
                styleName: item.styleName,
                canvasFontFamily: item.canvasFontFamily,
              })
            }
            onRemove={handleRemoveHistory}
            onClearAll={handleClearHistory}
            onReuseSource={(text) => {
              setInputText(text);
              setActiveTab('studio');
            }}
            onGoToStudio={() => setActiveTab('studio')}
          />
        )}

        {activeTab === 'android' && <AndroidGuideView />}
      </main>

      <MobileBottomNav
        activeTab={activeTab}
        onTabChange={setActiveTab}
        favoritesCount={favorites.length}
        historyCount={history.length}
      />

      <ShareSheetModal
        isOpen={!!shareTarget}
        text={shareTarget?.text ?? ''}
        styleName={shareTarget?.styleName ?? ''}
        onClose={() => setShareTarget(null)}
        onNotify={showToast}
        onRecordShare={() => {
          if (!shareTarget) return;
          setHistory((prev) =>
            appendHistoryItem(prev, {
              sourceText: effectiveText,
              styledText: shareTarget.text,
              styleId: shareTarget.styleId,
              styleName: shareTarget.styleName,
              script: shareTarget.script,
              fontClass: shareTarget.fontClass,
              canvasFontFamily: shareTarget.canvasFontFamily,
              actionType: 'shared',
            })
          );
        }}
      />

      <PngStudioModal
        isOpen={!!pngTarget}
        initialText={pngTarget?.text ?? ''}
        styleName={pngTarget?.styleName ?? ''}
        canvasFontFamily={pngTarget?.canvasFontFamily ?? '"Inter", sans-serif'}
        onClose={() => setPngTarget(null)}
        onNotify={showToast}
        onRecordExport={(exportedText, sName) => {
          if (!pngTarget) return;
          setHistory((prev) =>
            appendHistoryItem(prev, {
              sourceText: effectiveText,
              styledText: exportedText,
              styleId: pngTarget.styleId,
              styleName: sName,
              script: 'both',
              canvasFontFamily: pngTarget.canvasFontFamily,
              actionType: 'exported',
            })
          );
        }}
      />

      {/* Toast Feedback */}
      {toastMessage && (
        <div
          role="status"
          aria-live="polite"
          className="fixed bottom-18 md:bottom-6 left-1/2 z-50 -translate-x-1/2 rounded-full border px-4 py-2 text-xs font-grotesk font-semibold shadow-lg whitespace-nowrap"
          style={{
            backgroundColor: 'var(--surface-elevated)',
            borderColor: 'var(--accent-gold)',
            color: 'var(--text-primary)',
          }}
        >
          {toastMessage}
        </div>
      )}
    </div>
  );
}
