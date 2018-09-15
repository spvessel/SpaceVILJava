using System;
using System.Drawing;
using System.Linq;
using System.Collections.Generic;

namespace SpaceVIL
{
    public class TreeView : ListBox
    {
        public EventCommonMethod EventSortTree;
        private ContextMenu _menu;
        internal TreeItem _root; //nesting level = 0
        public void SetRootVisibility(bool visible)
        {
            _root.IsVisible = visible;
            //reset all paddings for content
            List<BaseItem> list = GetListContent();
            if (list != null)
            {
                foreach (var item in list)
                {
                    TreeItem tmp = item as TreeItem;
                    if(tmp != null)
                        tmp.ResetIndents();
                }
            }
            UpdateElements();
        }
        static int count = 0;
        public TreeView()
        {
            SetItemName("TreeView_" + count);
            count++;
            _root = new TreeItem(TreeItemType.Branch, "root");

            //SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.TreeView)));
            EventSortTree += OnSortTree;
        }
        public override void InitElements()
        {
            base.InitElements();
            SetSelectionVisibility(true);
            _root._parent = this;
            _root.IsRoot = true;
            AddItem(_root);
            SetRootVisibility(false);

            _menu = new ContextMenu(GetHandler());
            _menu.SetBackground(40, 40, 40);
            _menu.IsPassEvents = false;
            MenuItem paste = new MenuItem("Paste");
            paste.SetForeground(Color.LightGray);
            paste.AddItemState(ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(80, 255, 255, 255)
            });
            paste.EventMouseClick += (sender, args) =>
            {
                //paste
            };
            MenuItem new_leaf = new MenuItem("New Leaf");
            new_leaf.SetForeground(Color.LightGray);
            new_leaf.AddItemState(ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(80, 255, 255, 255)
            });
            new_leaf.EventMouseClick += (sender, args) =>
            {
                AddTreeItem(GetTreeLeaf());
            };
            MenuItem new_branch = new MenuItem("New Branch");
            new_branch.SetForeground(Color.LightGray);
            new_branch.AddItemState(ItemStateType.Hovered, new ItemState()
            {
                Background = Color.FromArgb(80, 255, 255, 255)
            });
            new_branch.EventMouseClick += (sender, args) =>
            {
                AddTreeItem(GetTreeBranch());
            };

            EventMouseClick += (_, x) => _menu.Show(_, x);

            _menu.SetSize(100, 4 + 30 * 3 - 5);
            _menu.AddItems(new_branch, new_leaf, paste);
        }
        private TreeItem GetTreeBranch()
        {
            TreeItem item = new TreeItem(TreeItemType.Branch);
            item.SetText(item.GetItemName());
            return item;
        }
        private TreeItem GetTreeLeaf()
        {
            TreeItem item = new TreeItem(TreeItemType.Leaf);
            item.SetText(item.GetItemName());
            return item;
        }
        internal void RefreshTree(TreeItem item)
        {
            AddItem(item);
            OnSortTree();
            UpdateElements();
        }
        protected virtual void OnSortTree()
        {
            //sorting
            List<TreeItem> outList = new List<TreeItem>();
            outList.Add(_root);
            outList.AddRange(SortHelper(_root));

            SetListContent(outList.Select(_ => _ as BaseItem).ToList());
        }

        private List<TreeItem> SortHelper(TreeItem item) 
        {
            List<TreeItem> tmpList = item.GetTreeItems();
            tmpList.Sort(CompareInAlphabet);
            List<TreeItem> outList = new List<TreeItem>();
            foreach (TreeItem ti in tmpList)
            {
                outList.Add(ti);
                if (ti.GetItemType() == TreeItemType.Branch)
                    outList.AddRange(SortHelper(ti));
            }

            return outList;
        }

        private int CompareInAlphabet(TreeItem ti1, TreeItem ti2)
        {
            if (ti1.GetItemType() != ti2.GetItemType())
            {
                if (ti1.GetItemType() == TreeItemType.Branch) return -1;
                else return 1;
            }

            return ti1.GetText().CompareTo(ti2.GetText());
        }

        public void AddTreeItem(TreeItem item)
        {
            _root.AddTreeItem(item);
        }
        public override void AddItem(BaseItem item)
        {
            base.AddItem(item);
        }

        public override void SetStyle(Style style)
        {
            if (style == null)
                return;
            base.SetStyle(style);
            //additional
        }
    }
}