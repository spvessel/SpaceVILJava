using System.Drawing;

namespace SpaceVIL
{
    public class Binder : Prototype, IDraggable
    {
        static int count = 0;
        public Binder()
        {
            SetItemName("Binder_" + count);
            count++;

            SetStyle(DefaultsService.GetDefaultStyle(typeof(SpaceVIL.Binder)));
        }
    }
} 