import { SavedTextItem, ThemeMode } from '../types/typography';

const FAVORITES_KEY = 'help_boldigraphi_favorites_v1';
const HISTORY_KEY = 'help_boldigraphi_history_v1';
const THEME_KEY = 'help_boldigraphi_theme_v1';
const DRAFT_KEY = 'help_boldigraphi_draft_v1';

const MAX_HISTORY_ITEMS = 60;

function safeReadJson<T>(key: string, fallback: T): T {
  try {
    const raw = window.localStorage.getItem(key);
    if (!raw) return fallback;
    return JSON.parse(raw) as T;
  } catch {
    return fallback;
  }
}

function safeWriteJson<T>(key: string, value: T): boolean {
  try {
    window.localStorage.setItem(key, JSON.stringify(value));
    return true;
  } catch {
    return false;
  }
}

export function loadFavorites(): SavedTextItem[] {
  const items = safeReadJson<SavedTextItem[]>(FAVORITES_KEY, []);
  return Array.isArray(items) ? items : [];
}

export function saveFavorites(items: SavedTextItem[]): boolean {
  return safeWriteJson(FAVORITES_KEY, items);
}

export function loadHistory(): SavedTextItem[] {
  const items = safeReadJson<SavedTextItem[]>(HISTORY_KEY, []);
  return Array.isArray(items) ? items : [];
}

export function saveHistory(items: SavedTextItem[]): boolean {
  return safeWriteJson(HISTORY_KEY, items.slice(0, MAX_HISTORY_ITEMS));
}

export function appendHistoryItem(
  current: SavedTextItem[],
  item: Omit<SavedTextItem, 'id' | 'createdAt'>
): SavedTextItem[] {
  const deduplicated = current.filter(
    (entry) => !(entry.styledText === item.styledText && entry.styleId === item.styleId)
  );
  const next: SavedTextItem[] = [
    {
      ...item,
      id: `${Date.now()}-${Math.random().toString(36).slice(2, 8)}`,
      createdAt: Date.now(),
    },
    ...deduplicated,
  ].slice(0, MAX_HISTORY_ITEMS);
  saveHistory(next);
  return next;
}

export function loadTheme(): ThemeMode {
  try {
    const raw = window.localStorage.getItem(THEME_KEY);
    return raw === 'light' ? 'light' : 'dark';
  } catch {
    return 'dark';
  }
}

export function saveTheme(theme: ThemeMode): void {
  try {
    window.localStorage.setItem(THEME_KEY, theme);
  } catch {
    // Ignore storage quota errors
  }
}

export function loadDraftText(): string {
  try {
    const raw = window.localStorage.getItem(DRAFT_KEY);
    return raw !== null ? raw : 'HELP BOLDIGRAPHI\nआपका स्वागत है';
  } catch {
    return 'HELP BOLDIGRAPHI\nआपका स्वागत है';
  }
}

export function saveDraftText(text: string): void {
  try {
    window.localStorage.setItem(DRAFT_KEY, text);
  } catch {
    // Ignore storage quota errors
  }
}
