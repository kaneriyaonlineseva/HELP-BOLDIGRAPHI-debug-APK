import React from 'react';
import { Clock, Smartphone, Sparkles, Star } from 'lucide-react';
import { AppTab } from '../types/typography';

interface MobileBottomNavProps {
  activeTab: AppTab;
  onTabChange: (tab: AppTab) => void;
  favoritesCount: number;
  historyCount: number;
}

export const MobileBottomNav: React.FC<MobileBottomNavProps> = ({
  activeTab,
  onTabChange,
  favoritesCount,
  historyCount,
}) => {
  const items: { id: AppTab; label: string; icon: React.ComponentType<{ className?: string }>; badge?: number }[] = [
    { id: 'studio', label: 'Studio', icon: Sparkles },
    { id: 'favorites', label: 'Favorites', icon: Star, badge: favoritesCount },
    { id: 'history', label: 'History', icon: Clock, badge: historyCount },
    { id: 'android', label: 'Android', icon: Smartphone },
  ];

  return (
    <nav
      aria-label="Mobile navigation"
      className="md:hidden fixed bottom-0 left-0 right-0 z-30 h-14 border-t backdrop-blur-md"
      style={{
        backgroundColor: 'var(--surface-low)',
        borderColor: 'var(--border-subtle)',
      }}
    >
      <div className="grid h-full grid-cols-4">
        {items.map((item) => {
          const Icon = item.icon;
          const active = activeTab === item.id;
          return (
            <button
              key={item.id}
              type="button"
              onClick={() => onTabChange(item.id)}
              className="relative flex flex-col items-center justify-center gap-0.5 text-[11px] font-grotesk font-semibold transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-inset focus-visible:ring-violet-500"
              style={{
                color: active ? 'var(--accent-violet)' : 'var(--text-secondary)',
              }}
            >
              <Icon className="h-4 w-4" aria-hidden="true" />
              <span>{item.label}</span>
              {typeof item.badge === 'number' && item.badge > 0 && (
                <span
                  className="absolute top-1.5 right-4 font-grotesk text-[10px] tabular-nums"
                  style={{ color: 'var(--accent-gold)' }}
                >
                  {item.badge}
                </span>
              )}
            </button>
          );
        })}
      </div>
    </nav>
  );
};
