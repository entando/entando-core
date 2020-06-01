package org.entando.entando.aps.system.services.mockhelper;

import com.agiletec.aps.system.services.lang.ILangManager;
import org.entando.entando.aps.system.services.guifragment.GuiFragment;
import org.entando.entando.aps.system.services.guifragment.model.GuiFragmentDto;

public class FragmentMockHelper {

    public static final String FRAGMENT_CODE = "fragment1";
    public static final String FRAGMENT_REF_1_CODE = "fragment_ref_1";
    public static final String FRAGMENT_REF_2_CODE = "fragment_ref_2";

    public static GuiFragment mockGuiFragment() {
        return mockGuiFragment(FRAGMENT_CODE);
    }

    public static GuiFragment mockGuiFragment(String fragmentCode) {
        GuiFragment fragment = new GuiFragment();
        fragment.setCode(fragmentCode);
        fragment.setWidgetTypeCode(WidgetMockHelper.WIDGET_1_CODE);
        return fragment;
    }

    public static GuiFragmentDto mockGuiFragmentDto(GuiFragment guiFragment, ILangManager langManager) {
        return mockGuiFragmentDto(FRAGMENT_CODE, guiFragment, langManager);
    }

    public static GuiFragmentDto mockGuiFragmentDto(String fragmentCode, GuiFragment guiFragment, ILangManager langManager) {

        GuiFragmentDto fragmentDto = new GuiFragmentDto(guiFragment, WidgetMockHelper.mockWidgetType(), langManager);
        fragmentDto.setCode(fragmentCode);

        fragmentDto.addFragmentRef(mockGuiFragment(FRAGMENT_REF_1_CODE));
        fragmentDto.addFragmentRef(mockGuiFragment(FRAGMENT_REF_2_CODE));

        fragmentDto.addPageModelRef(PageMockHelper.mockPageModel(PageMockHelper.PAGE_MODEL_REF_CODE_1));
        fragmentDto.addPageModelRef(PageMockHelper.mockPageModel(PageMockHelper.PAGE_MODEL_REF_CODE_2));

        return fragmentDto;
    }
}
