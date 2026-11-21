export type ScriptFilter = 'all' | 'english' | 'hindi';

export type StyleCategory =
  | 'all'
  | 'bold'
  | 'script'
  | 'gothic'
  | 'decorative'
  | 'devanagari';

export type ThemeMode = 'dark' | 'light';

export type AppTab = 'studio' | 'favorites' | 'history' | 'android';

export interface StylePreset {
  id: string;
  name: string;
  subtitle: string;
  script: 'english' | 'hindi' | 'both';
  category: Exclude<StyleCategory, 'all'>;
  renderMode: 'unicode' | 'font' | 'ornament';
  fontClass?: string;
  canvasFontFamily: string;
  isVip?: boolean;
  transform: (input: string) => string;
}

export interface SavedTextItem {
  id: string;
  sourceText: string;
  styledText: string;
  styleId: string;
  styleName: string;
  script: 'english' | 'hindi' | 'both';
  fontClass?: string;
  canvasFontFamily: string;
  createdAt: number;
  actionType?: 'copied' | 'shared' | 'exported' | 'favorited';
}

export type PngThemePresetId =
  | 'obsidian-violet'
  | 'royal-gold'
  | 'midnight-ink'
  | 'crimson-velvet'
  | 'ivory-luxe';

export type PngAspectRatio = '1:1' | '4:5' | '16:9';

export type TextAlignment = 'left' | 'center' | 'right';

export interface PngExportConfig {
  text: string;
  styleName: string;
  canvasFontFamily: string;
  themePreset: PngThemePresetId;
  aspectRatio: PngAspectRatio;
  fontSize: number;
  alignment: TextAlignment;
  showWatermark: boolean;
}
