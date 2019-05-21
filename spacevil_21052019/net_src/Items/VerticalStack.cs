using System;
using System.Drawing;
using SpaceVIL.Common;
using SpaceVIL.Core;
using System.Collections.Generic;

namespace SpaceVIL
{
    public class VerticalStack : Prototype, IVLayout
    {
        static int count = 0;

        private ItemAlignment _contentAlignment = ItemAlignment.Top;

        public void SetContentAlignment(ItemAlignment alignment)
        {
            if (alignment == ItemAlignment.Top || alignment == ItemAlignment.VCenter || alignment == ItemAlignment.Bottom)
                _contentAlignment = alignment;
        }

        public ItemAlignment GetContentAlignment()
        {
            return _contentAlignment;
        }

        public VerticalStack()
        {
            SetItemName("VerticalStack_" + count);
            count++;
            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.VerticalStack)));
            IsFocusable = false;

        }

        //overrides
        protected internal override bool GetHoverVerification(float xpos, float ypos)
        {
            return false;
        }

        public override void AddItem(IBaseItem item)
        {
            base.AddItem(item);
            UpdateLayout();
        }
        public override void InsertItem(IBaseItem item, int index)
        {
            base.InsertItem(item, index);
            UpdateLayout();
        }

        public override bool RemoveItem(IBaseItem item)
        {
            bool result = base.RemoveItem(item);
            if (result)
                UpdateLayout();
            return result;
        }
        public override void SetHeight(int height)
        {
            base.SetHeight(height);
            UpdateLayout();
        }
        public override void SetY(int _y)
        {
            base.SetY(_y);
            UpdateLayout();
        }

        public void UpdateLayout()
        {
            int total_space = GetHeight() - GetPadding().Top - GetPadding().Bottom;
            int free_space = total_space;
            int fixed_count = 0;
            int expanded_count = 0;

            List<int> maxHeightExpands = new List<int>();

            foreach (var child in GetItems())
            {
                if (child.IsVisible())
                {
                    if (child.GetHeightPolicy() == SizePolicy.Fixed)
                    {
                        fixed_count++;
                        free_space -= (child.GetHeight() + child.GetMargin().Top + child.GetMargin().Bottom);//
                    }
                    else
                    {
                        if (child.GetMaxHeight() < total_space) {
                            maxHeightExpands.Add(child.GetMaxHeight() + child.GetMargin().Top + child.GetMargin().Bottom);
                        }
                        expanded_count++;
                    }
                }
            }
            free_space -= GetSpacing().Vertical * ((expanded_count + fixed_count) - 1);

            int height_for_expanded = 1;
            // if (expanded_count > 0 && free_space > expanded_count)
            //     height_for_expanded = free_space / expanded_count;

            maxHeightExpands.Sort();

            while (true) {
                if (expanded_count == 0)
                    break;

                if (free_space > expanded_count)
                    height_for_expanded = free_space / expanded_count;

                if (height_for_expanded <= 1 || maxHeightExpands.Count == 0) {
                    break;
                }

                if (height_for_expanded > maxHeightExpands[0]) {
                    while (maxHeightExpands.Count > 0 && height_for_expanded > maxHeightExpands[0]) {
                        free_space -= maxHeightExpands[0];
                        maxHeightExpands.RemoveAt(0);
                        expanded_count--;
                    }
                } else {
                    break;
                }
            }

            int offset = 0;
            int startY = GetY() + GetPadding().Top;
            bool isFirstExpand = false;
            int diff = (free_space - height_for_expanded * expanded_count);
            if (expanded_count != 0 && diff > 0) {
                isFirstExpand = true;
            }

            if (expanded_count > 0 || _contentAlignment.Equals(ItemAlignment.Top))
            {
                foreach (var child in GetItems())
                {
                    if (child.IsVisible())
                    {
                        child.SetY(startY + offset + child.GetMargin().Top);//
                        if (child.GetHeightPolicy() == SizePolicy.Expand)
                        {
                            if (height_for_expanded - child.GetMargin().Top - child.GetMargin().Bottom < child.GetMaxHeight())//
                            {
                                child.SetHeight(height_for_expanded - child.GetMargin().Top - child.GetMargin().Bottom);//
                            }
                            else
                            {
                                // expanded_count--;
                                // free_space -= (child.GetHeight() + child.GetMargin().Top + child.GetMargin().Bottom);
                                // height_for_expanded = 1;
                                // if (expanded_count > 0 && free_space > expanded_count)
                                //     height_for_expanded = free_space / expanded_count;
                                child.SetHeight(child.GetMaxHeight()); //?
                            }

                            if (isFirstExpand) {
                                if (child.GetHeight() + diff < child.GetMaxHeight()) {
                                    child.SetHeight(child.GetHeight() + diff);
                                    isFirstExpand = false;
                                }
                            }
                        }
                        offset += child.GetHeight() + GetSpacing().Vertical + child.GetMargin().Top + child.GetMargin().Bottom;//
                    }
                    //refactor
                    child.SetConfines();
                }
            }
            else
            {
                //for content alignment right
                if (_contentAlignment.Equals(ItemAlignment.Bottom))
                {
                    foreach (var child in GetItems())
                    {
                        if (child.IsVisible())
                        {
                            child.SetY(startY + offset + child.GetMargin().Top + free_space);
                            offset += child.GetHeight() + GetSpacing().Vertical + child.GetMargin().Top + child.GetMargin().Bottom;//

                            if (child.GetHeightPolicy() == SizePolicy.Expand)
                                child.SetHeight(child.GetMaxHeight());
                        }
                        child.SetConfines();
                    }
                }
                //for content alignment hcenter
                else if (_contentAlignment.Equals(ItemAlignment.VCenter))
                {
                    foreach (var child in GetItems())
                    {
                        if (child.IsVisible())
                        {
                            child.SetY(startY + offset + child.GetMargin().Top + free_space / 2);
                            offset += child.GetHeight() + GetSpacing().Vertical + child.GetMargin().Top + child.GetMargin().Bottom;//

                            if (child.GetHeightPolicy() == SizePolicy.Expand)
                                child.SetHeight(child.GetMaxHeight());
                        }
                        child.SetConfines();
                    }
                }
            }
        }
    }
}