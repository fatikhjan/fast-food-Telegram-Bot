package uz.company.container;

import uz.company.controller.AdminController;
import uz.company.controller.CureerController;
import uz.company.controller.UserController;
import uz.company.db.DataStore;
import uz.company.mappers.UserThingsMapper;
import uz.company.model.Product;
import uz.company.model.User;
import uz.company.service.BasicService;
import uz.company.service.UserService;
import uz.company.util.AdminInlineKeyboards;
import uz.company.util.UserInlineKeybordUtil;
import uz.company.util.UserKeybordUtil;

public class ThreadSafeBeanContext {

    public static final ThreadLocal<AdminController> ADMIN_CONTROLLER_THREAD_LOCAL = ThreadLocal.withInitial(AdminController::new);

    public static final ThreadLocal<UserController> USER_CONTROLLER_THREAD_LOCAL = ThreadLocal.withInitial(UserController::new);

    public static final ThreadLocal<CureerController> CUREER_CONTROLLER_THREAD_LOCAL = ThreadLocal.withInitial(CureerController::new);

    public static final ThreadLocal<DataStore> DATA_STORE_THREAD_LOCAL = ThreadLocal.withInitial(DataStore::new);

    public static final ThreadLocal<Product> PRODUCT_THREAD_LOCAL = ThreadLocal.withInitial(Product::new);

    public static final ThreadLocal<User> USER_THREAD_LOCAL = ThreadLocal.withInitial(User::new);

    public static final ThreadLocal<BasicService> BASIC_SERVICE_THREAD_LOCAL = ThreadLocal.withInitial(BasicService::new);

    public static final ThreadLocal<UserService> USER_SERVICE_THREAD_LOCAL = ThreadLocal.withInitial(UserService::new);

    public static final ThreadLocal<AdminInlineKeyboards> ADMIN_INLINE_KEYBOARDS_THREAD_LOCAL = ThreadLocal.withInitial(AdminInlineKeyboards::new);

    public static final ThreadLocal<UserInlineKeybordUtil> USER_INLINE_KEYBORD_UTIL_THREAD_LOCAL = ThreadLocal.withInitial(UserInlineKeybordUtil::new);

    public static final ThreadLocal<UserKeybordUtil> USER_KEYBORD_UTIL_THREAD_LOCAL = ThreadLocal.withInitial(UserKeybordUtil::new);
    public static final ThreadLocal<UserThingsMapper> USER_THINGS_MAPPER_THREAD_LOCAL = ThreadLocal.withInitial(UserThingsMapper::new);


}
