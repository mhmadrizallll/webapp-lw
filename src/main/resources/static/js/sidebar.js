document.addEventListener("DOMContentLoaded", function () {

  const sidebar = document.getElementById('sidebar');
  const viewer = document.getElementById('pdfViewer');
  const resizer = document.getElementById('resizer');
  const MIN = 150, MAX = 900;

  // ===========================
  // 🔹 Section & Folder State
  // ===========================
  function getCurrentSection() {
    const params = new URLSearchParams(window.location.search);
    return params.get('secondary') || 'UNKNOWN';
  }

  function saveOpenFolders() {
    const openFolders = [];
    document.querySelectorAll('.tree ul:not(.hidden)').forEach(folder => {
      openFolders.push(folder.id);
    });
    const section = getCurrentSection();
    localStorage.setItem(`openFolders_${section}`, JSON.stringify(openFolders));
  }

  function restoreOpenFolders() {
    const section = getCurrentSection();
    const openFolders = JSON.parse(localStorage.getItem(`openFolders_${section}`) || '[]');
    openFolders.forEach(id => {
      const ul = document.getElementById(id);
      if (ul) {
        ul.classList.remove('hidden');
        document.querySelectorAll(`[data-target="${id}"]`).forEach(t => t.classList.add('open'));
      }
    });
  }

  // Jika berpindah section, reset folder sebelumnya
  const current = getCurrentSection();
  const last = localStorage.getItem('lastSection');
  if (last && last !== current) {
    localStorage.removeItem(`openFolders_${last}`);
  }
  localStorage.setItem('lastSection', current);

  // ===========================
  // 🔹 Toggle Folder
  // ===========================
  document.querySelectorAll('.folder-toggle').forEach(toggle => {
    const targetId = toggle.dataset.target;
    const content = document.getElementById(targetId);
    if (!content) return;

    if (!content.classList.contains('hidden')) toggle.classList.add('open');

    toggle.addEventListener('click', () => {
      const isHidden = content.classList.toggle('hidden');
      toggle.classList.toggle('open', !isHidden);
      saveOpenFolders();
    });
  });

  restoreOpenFolders();

  // ===========================
  // 🔹 File Click
  // ===========================
  document.querySelectorAll('.file-link').forEach(link => {
    link.addEventListener('click', () => {
      const filePath = link.getAttribute('data-file');
      if (!filePath) return alert("File tidak ditemukan!");
      
      // viewer path relatif
      const fullPath = `../../${filePath}`;
      const encoded = encodeURIComponent(fullPath);

      if (viewer) viewer.src = `pdfjs/web/viewer.html?file=${encoded}`;
    });
  });

  // ===========================
  // 🔹 Search Filter
  // ===========================
  const searchInput = document.querySelector('.search-box');
  searchInput?.addEventListener('input', function () {
    const query = this.value.toLowerCase();
    document.querySelectorAll('.filterable-item').forEach(block => {
      const ul = block.querySelector('ul');
      const toggles = block.querySelectorAll('.folder-toggle');

      if (!query) {
        block.style.display = '';
        ul?.querySelectorAll('li').forEach(li => li.style.display = '');
        restoreOpenFolders();
        return;
      }

      let matched = block.textContent.toLowerCase().includes(query);
      if (ul) {
        let hasVisibleChild = false;
        ul.querySelectorAll('li').forEach(li => {
          const link = li.querySelector('.file-link');
          const isMatch = link?.textContent.toLowerCase().includes(query);
          li.style.display = isMatch ? '' : 'none';
          if (isMatch) hasVisibleChild = true;
        });
        matched = matched || hasVisibleChild;
        if (matched) {
          ul.classList.remove('hidden');
          toggles.forEach(t => t.classList.add('open'));
        } else {
          ul.classList.add('hidden');
          toggles.forEach(t => t.classList.remove('open'));
        }
      }

      block.style.display = matched ? '' : 'none';
    });

    saveOpenFolders();
  });

  // ===========================
  // 🔹 Drag Sidebar
  // ===========================
  let startX = 0, startW = 0, overlay = null;
  const saved = +localStorage.getItem('sidebarWidth');
  if (saved) sidebar.style.width = saved + 'px';

  function onMove(e) {
    const x = e.clientX ?? e.touches?.[0]?.clientX;
    const newW = Math.max(MIN, Math.min(MAX, startW + (x - startX)));
    sidebar.style.width = newW + 'px';
  }

  function endDrag() {
    window.removeEventListener('mousemove', onMove, true);
    window.removeEventListener('mouseup', endDrag, true);
    window.removeEventListener('touchmove', onMove, true);
    window.removeEventListener('touchend', endDrag, true);

    document.body.classList.remove('dragging');
    if (viewer) viewer.style.pointerEvents = '';
    overlay?.remove();

    localStorage.setItem('sidebarWidth', sidebar.offsetWidth);
  }

  function beginDrag(clientX) {
    startX = clientX;
    startW = sidebar.getBoundingClientRect().width;

    window.addEventListener('mousemove', onMove, true);
    window.addEventListener('mouseup', endDrag, true);
    window.addEventListener('touchmove', onMove, true);
    window.addEventListener('touchend', endDrag, true);

    document.body.classList.add('dragging');
    if (viewer) viewer.style.pointerEvents = 'none';
    overlay = document.createElement('div');
    overlay.className = 'drag-overlay';
    document.body.appendChild(overlay);
  }

  resizer?.addEventListener('mousedown', e => { e.preventDefault(); beginDrag(e.clientX); });
  resizer?.addEventListener('touchstart', e => beginDrag(e.touches[0].clientX), { passive: true });

});