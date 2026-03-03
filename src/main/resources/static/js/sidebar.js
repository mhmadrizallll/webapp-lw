document.addEventListener("DOMContentLoaded", function () {

  const sidebar = document.getElementById('sidebar');
  const viewer = document.getElementById('pdfViewer');
  const resizer = document.getElementById('resizer');
  const MIN = 150, MAX = 900;

  // ===========================
  // 🔹 Section Helper
  // ===========================
  function getCurrentSection() {
    const params = new URLSearchParams(window.location.search);
    return params.get('secondary') || 'UNKNOWN';
  }

  // ===========================
  // 🔹 Save & Restore Folder State
  // ===========================
  function saveOpenFolders() {
    const openFolders = [];

    document.querySelectorAll('.tree ul').forEach((ul, index) => {
      if (!ul.classList.contains('hidden')) {
        openFolders.push(index);
      }
    });

    const section = getCurrentSection();
    localStorage.setItem(`openFolders_${section}`, JSON.stringify(openFolders));
  }

  function restoreOpenFolders() {
    const section = getCurrentSection();
    const openFolders = JSON.parse(
      localStorage.getItem(`openFolders_${section}`) || '[]'
    );

    document.querySelectorAll('.tree ul').forEach((ul, index) => {
      if (openFolders.includes(index)) {
        ul.classList.remove('hidden');
      }
    });
  }

  // Reset jika pindah section
  const current = getCurrentSection();
  const last = localStorage.getItem('lastSection');
  if (last && last !== current) {
    localStorage.removeItem(`openFolders_${last}`);
  }
  localStorage.setItem('lastSection', current);

  restoreOpenFolders();

  // ===========================
  // 🔹 CLICK HANDLER (FOLDER + FILE)
  // ===========================
  document.addEventListener('click', function (e) {

    // ===== Folder Toggle FIXED =====
    const toggle = e.target.closest('.folder-toggle');
    if (toggle) {

      // Ambil UL tepat setelah folder-header
      const content = toggle.nextElementSibling;

      if (!content || content.tagName !== 'UL') return;

      const isHidden = content.classList.toggle('hidden');
      toggle.classList.toggle('open', !isHidden);

      saveOpenFolders();
      return;
    }

    // ===== File Click =====
    const fileElement = e.target.closest('.file-link');
    if (fileElement) {

      const filePath = fileElement.getAttribute('data-file');
      if (!filePath) {
        alert("File tidak ditemukan!");
        return;
      }

      const extension = filePath.split('.').pop().toLowerCase();

      // ===== Jika PDF → buka di PDFJS =====
      if (extension === "pdf") {

        const encoded = encodeURIComponent(filePath);

        if (viewer) {
          viewer.src = `/pdfjs/web/viewer.html?file=${encoded}`;
        }
      }

      // ===== Selain PDF → langsung download =====
      else {
        window.location.href = `/download?path=${encodeURIComponent(filePath)}`;
      }

      return;
    }

  });

  // ===========================
  // 🔹 Search Recursive
  // ===========================
  const searchInput = document.querySelector('.search-box');

  searchInput?.addEventListener('input', function () {

    const query = this.value.toLowerCase();

    document.querySelectorAll('.tree li').forEach(li => {

      if (!query) {
        li.style.display = '';
        restoreOpenFolders();
        return;
      }

      const text = li.textContent.toLowerCase();
      const matched = text.includes(query);

      li.style.display = matched ? '' : 'none';

      if (matched) {
        let parent = li.closest('ul');
        while (parent && parent.classList.contains('hidden')) {
          parent.classList.remove('hidden');
          parent = parent.parentElement.closest('ul');
        }
      }

    });

    saveOpenFolders();
  });

  // ===========================
  // 🔹 Drag Sidebar Resize
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

  resizer?.addEventListener('mousedown', e => {
    e.preventDefault();
    beginDrag(e.clientX);
  });

  resizer?.addEventListener('touchstart', e => {
    beginDrag(e.touches[0].clientX);
  }, { passive: true });

});