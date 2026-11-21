import React from 'react';
import { Search, X } from 'lucide-react';
import { CATEGORY_OPTIONS } from '../data/styleCatalog';
import { StyleCategory } from '../types/typography';

interface FilterToolbarProps {
  searchQuery: string;
  onSearchChange: (q: string) => void;
  selectedCategory: StyleCategory;
  onCategoryChange: (cat: StyleCategory) => void;
  resultCount: number;
}

export const FilterToolbar: React.FC<FilterToolbarProps> = ({
  searchQuery,
  onSearchChange,
  selectedCategory,
  onCategoryChange,
  resultCount,
}) => {
  return (
    <div className="space-y-3">
      {/* Search Bar + Count */}
      <div className="flex flex-col sm:flex-row sm:items-center gap-3">
        <div
          className="obsidian-halo relative flex flex-1 items-center rounded-2xl border px-3.5 transition-colors"
          style={{
            backgroundColor: 'var(--surface-card)',
            borderColor: 'var(--border-subtle)',
          }}
        >
          <Search
            className="h-4 w-4 shrink-0"
            style={{ color: 'var(--text-muted)' }}
            aria-hidden="true"
          />
          <label htmlFor="style-search-input" className="sr-only">
            Search font styles
          </label>
          <input
            id="style-search-input"
            type="text"
            value={searchQuery}
            onChange={(e) => onSearchChange(e.target.value)}
            placeholder="Search styles (e.g., Gothic, Rozha, Cursive, Bubble)..."
            className="min-h-[44px] w-full bg-transparent px-3 text-sm outline-none placeholder:opacity-60"
            style={{ color: 'var(--text-primary)' }}
          />
          {searchQuery && (
            <button
              type="button"
              onClick={() => onSearchChange('')}
              aria-label="Clear search"
              className="inline-flex min-h-[32px] min-w-[32px] items-center justify-center rounded-full hover:bg-violet-500/15"
              style={{ color: 'var(--text-secondary)' }}
            >
              <X className="h-4 w-4" aria-hidden="true" />
            </button>
          )}
        </div>

        <div
          className="font-grotesk text-xs font-medium tabular-nums whitespace-nowrap px-1"
          style={{ color: 'var(--text-secondary)' }}
        >
          {resultCount} {resultCount === 1 ? 'style' : 'styles'} available
        </div>
      </div>

      {/* Category Filter Bar */}
      <div
        className="no-scrollbar flex items-center gap-1.5 overflow-x-auto rounded-2xl border p-1.5"
        style={{
          backgroundColor: 'var(--surface-low)',
          borderColor: 'var(--border-subtle)',
        }}
        role="tablist"
        aria-label="Font categories"
      >
        {CATEGORY_OPTIONS.map((cat) => {
          const active = selectedCategory === cat.id;
          return (
            <button
              key={cat.id}
              type="button"
              role="tab"
              aria-selected={active}
              onClick={() => onCategoryChange(cat.id)}
              className={`min-h-[38px] rounded-full px-4 py-1.5 text-xs font-grotesk font-semibold whitespace-nowrap shrink-0 transition-all focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-violet-500 ${
                active ? 'text-white shadow-sm' : 'hover:opacity-90'
              }`}
              style={
                active
                  ? { background: 'linear-gradient(135deg, #8B5CF6 0%, #7C3AED 100%)' }
                  : { color: 'var(--text-secondary)' }
              }
            >
              {cat.label}
            </button>
          );
        })}
      </div>
    </div>
  );
};
