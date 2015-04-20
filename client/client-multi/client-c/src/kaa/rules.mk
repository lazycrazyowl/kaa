# Standard things
sp := $(sp).x
dirstack_$(sp) := $(d)
d := $(dir)
BUILDDIRS += $(BUILD_PATH)/$(d)
BUILDDIRS += $(BUILD_PATH)/$(d)/utilities
BUILDDIRS += $(BUILD_PATH)/$(d)/platform-impl/stm32/leafMapleMini
BUILDDIRS += $(BUILD_PATH)/$(d)/platform-impl/stm32/leafMapleMini/esp8266
BUILDDIRS += $(BUILD_PATH)/$(d)/kaa_protocols/kaa_tcp
BUILDDIRS += $(BUILD_PATH)/$(d)/avro_src
BUILDDIRS += $(BUILD_PATH)/$(d)/collections
BUILDDIRS += $(BUILD_PATH)/$(d)/gen

# Local flags
CXXFLAGS_$(d) := $(WIRISH_INCLUDES) $(LIBMAPLE_INCLUDES)
CFLAGS_$(d) := $(WIRISH_INCLUDES) $(LIBMAPLE_INCLUDES) 
CFLAGS += -DSTM32_LEAF_PLATFORM

# Local rules and targets
cSRCS_$(d) :=  utilities/kaa_log.c \
               utilities/kaa_buffer.c \
               utilities/kaa_base64.c \
               platform-impl/stm32/leafMapleMini/logger.c \
               platform-impl/stm32/leafMapleMini/esp8266/esp8266.c \
               platform-impl/stm32/leafMapleMini/esp8266/esp8266_kaa_client.c \
               platform-impl/stm32/leafMapleMini/esp8266/esp8266_kaa_tcp_channel.c  \
               platform-impl/ext_log_storage_memory.c \
               platform-impl/ext_log_upload_strategy_by_volume.c \
               platform-impl/sha1.c \
               platform-impl/sha.c \
               kaa_protocols/kaa_tcp/kaatcp_parser.c \
               kaa_protocols/kaa_tcp/kaatcp_request.c \
               avro_src/encoding_binary.c \
               avro_src/io.c \
               collections/kaa_deque.c \
               collections/kaa_list.c \
               gen/kaa_configuration_gen.c \
               gen/kaa_logging_gen.c \
               gen/kaa_profile_gen.c \
               kaa_bootstrap_manager.c \
               kaa_channel_manager.c \
               kaa_common_schema.c \
               kaa_configuration_manager.c \
               kaa_event.c \
               kaa_logging.c \
               kaa_platform_protocol.c \
               kaa_platform_utils.c \
               kaa_profile.c \
               kaa_status.c \
               kaa_user.c \
               kaa.c \
               

#cppSRCS_$(d) := MapleFreeRTOS.cpp

cFILES_$(d) := $(cSRCS_$(d):%=$(d)/%)
cppFILES_$(d) := $(cppSRCS_$(d):%=$(d)/%)

OBJS_$(d) := $(cFILES_$(d):%.c=$(BUILD_PATH)/%.o) \
             $(cppFILES_$(d):%.cpp=$(BUILD_PATH)/%.o)
DEPS_$(d) := $(OBJS_$(d):%.o=%.d)

$(OBJS_$(d)): TGT_CXXFLAGS := $(CXXFLAGS_$(d))
$(OBJS_$(d)): TGT_CFLAGS := $(CFLAGS_$(d))

TGT_BIN += $(OBJS_$(d))

# Standard things
-include $(DEPS_$(d))
d := $(dirstack_$(sp))
sp := $(basename $(sp))
