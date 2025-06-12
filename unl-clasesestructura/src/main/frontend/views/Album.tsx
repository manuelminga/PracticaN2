import { ViewConfig } from '@vaadin/hilla-file-router/types.js';
import { Button, ComboBox, DatePicker, Dialog, Grid, GridColumn, GridItemModel, GridSortColumn, TextField, VerticalLayout, HorizontalLayout, Select, Icon } from '@vaadin/react-components';
import { Notification } from '@vaadin/react-components/Notification';

import { useSignal } from '@vaadin/hilla-react-signals';
import handleError from 'Frontend/views/_ErrorHandler';
import { Group, ViewToolbar } from 'Frontend/components/ViewToolbar';

import { useDataProvider } from '@vaadin/hilla-react-crud';
import { AlbumService} from 'Frontend/generated/endpoints';

import { useEffect, useState } from 'react';
import Album from 'Frontend/generated/com/unl/clasesestructura/base/models/Album';
import { order, updateAlbum } from 'Frontend/generated/AlbumService';

export const config: ViewConfig = {
  title: 'Album',
  menu: {
    icon: 'vaadin:clipboard-check',
    order: 4,
    title: 'Album',
  },
};

type AlbumEntryFormProps = {
  onAlbumCreated?: () => void;
};

type AlbumEntryFormUpdateProps = () => {
  onAlbumUpdate?: () => void;
};

// crear Album
function AlbumEntryForm(props: AlbumEntryFormProps) {
  const dialogOpened = useSignal(false);
  const [banda, setBanda] = useState<{ id: string, label: string }[]>([]);

  const open = () => {
    dialogOpened.value = true;
  };

  const close = () => {
    dialogOpened.value = false;
  };

  const nombre = useSignal('');
  const Banda = useSignal('');

  const createAlbum = async () => {
    try {
      console.log('Valores actuales:', {
        nombre: nombre.value,
        Banda: Banda.value
      });
      // Validar campos requeridos
      if (!nombre.value.trim() || !Banda.value.trim()) {
        Notification.show('Complete todos los campos requeridos',
          { duration: 5000, position: 'top-center', theme: 'error' });
        return;
      }




      // Crear álbum
      await AlbumService.createAlbum(
        nombre.value.trim(),
        Banda.value.trim(),
        parseInt(Banda.value)
      );


      // Limpiar y cerrar
      nombre.value = '';
      Banda.value = '';
      dialogOpened.value = false;

      // Notificar y refrescar
      Notification.show('Álbum creado exitosamente',
        { duration: 5000, position: 'bottom-end', theme: 'success' });
      props.onAlbumCreated?.();
    } catch (error) {
      console.error('Error al crear álbum:', error);
      Notification.show('Error al crear el álbum',
        { duration: 5000, position: 'top-center', theme: 'error' });
      handleError(error);
    }
  };

  // Cargar tipos de archivo y géneros
  useEffect(() => {

    AlbumService.ListaBandaCombo()
      .then((result) => setBanda(result.map(b => ({ id: b.id, label: b.label }))))
      .catch(console.error);
  }, []);

  return (
    <>
      <Dialog
        aria-label="Registrar Álbum"
        draggable
        modeless
        opened={dialogOpened.value}
        onOpenedChanged={(e) => dialogOpened.value = e.detail.value}
        header={
          <h2 style={{
            flex: 1,
            cursor: 'move',
            margin: 0,
            fontSize: '1.5em',
            fontWeight: 'bold',
            padding: 'var(--lumo-space-m) 0',
          }}>
            Registrar Álbum
          </h2>
        }
        footerRenderer={() => (
          <>
            <Button onClick={close}>Cancelar</Button>
            <Button theme="primary" onClick={createAlbum}>
              Registrar
            </Button>
          </>
        )}
      >
        <VerticalLayout style={{ width: '300px', maxWidth: '100%', alignItems: 'stretch' }}>
          <TextField
            label="Nombre"
            placeholder="Nombre del álbum"
            value={nombre.value}
            onValueChanged={(e) => nombre.value = e.detail.value}
            required
          />
            <ComboBox
                label="Banda"
                items={banda}
                itemValuePath="id"
                itemLabelPath="label"
                value={Banda.value}
                onValueChanged={(e) => {Banda.value = e.detail.value}}
                placeholder="Seleccione banda"
                required
            />
        </VerticalLayout>
      </Dialog>
      <Button onClick={open} theme="primary">
        ＋ Registrar Álbum
      </Button>
    </>
  );
}

////***************************** */
function AlbumEntryFormUpdate(props: AlbumEntryFormUpdateProps) {
  const dialogOpened = useSignal(false);
  const [banda, setBanda] = useState<{ id: string, label: string }[]>([]);

  const open = () => {
    dialogOpened.value = true;
  };

  const close = () => {
    dialogOpened.value = false;
  };

  const nombre = useSignal(props.arguments.nombre);
    const Banda = useSignal(props.arguments.id_banda?.toString() || '');
    const fecha = useSignal(props.arguments.fechaCreacion);
  

  const updateAlbum = async () => {
    try {
      if (nombre.value.trim().length > 0 && Banda.value.trim().length > 0 && fecha.value.trim().length > 0) {

        await AlbumService.updateAlbum(
          parseInt(props.arguments.id),
          nombre.value,
          fecha.value,
          parseInt(Banda.value)
        );

        if (props.onAlbumUpdate) {
          props.onAlbumUpdate();
        }

        dialogOpened.value = false;
        Notification.show('Álbum actualizado exitosamente',
          { duration: 5000, position: 'bottom-end', theme: 'success' });
      } else {
        Notification.show('No se pudo actualizar, faltan datos',
          { duration: 5000, position: 'top-center', theme: 'error' });
      }
    } catch (error) {
      console.log(error);
      handleError(error);
    }
  };

  useEffect(() => {
    AlbumService.ListaBandaCombo()
      .then((result) => setBanda(result.map(b => ({ id: b.id, label: b.label }))))
      .catch(console.error);
  }, []);



    return (
    <>
      <Dialog
        aria-label="Editar Álbum"
        draggable
        modeless
        opened={dialogOpened.value}
        onOpenedChanged={(event) => {
          dialogOpened.value = event.detail.value;
        }}
        header={
          <h2
            className="draggable"
            style={{
              flex: 1,
              cursor: 'move',
              margin: 0,
              fontSize: '1.5em',
              fontWeight: 'bold',
              padding: 'var(--lumo-space-m) 0',
            }}
          >
            Editar Álbum
          </h2>
        }
        footerRenderer={() => (
          <>
            <Button onClick={close}>Cancelar</Button>
            <Button theme="primary" onClick={updateAlbum}>
              Actualizar
            </Button>
          </>
        )}
      >
        <VerticalLayout
          theme="spacing"
          style={{ width: '300px', maxWidth: '100%', alignItems: 'stretch' }}
        >
          <VerticalLayout style={{ alignItems: 'stretch' }}>
            <TextField
              label="Nombre"
              placeholder='Ingrese el nombre del álbum'
              value={nombre.value}
              onValueChanged={(evt) => (nombre.value = evt.detail.value)}
            />
            <ComboBox
              label="Banda"
              items={banda}
              itemValuePath="id"
              itemLabelPath="label"
              value={Banda.value}
              onValueChanged={(e) => (Banda.value = e.detail.value)}
              placeholder="Seleccione banda"
            />
            <DatePicker
              label="Fecha de creación"
              placeholder="Seleccione una fecha"
              aria-label="Seleccione una fecha"
              value={fecha.value ?? undefined}
              onValueChanged={(evt) => (fecha.value = evt.detail.value)}
            />
          </VerticalLayout>
        </VerticalLayout>
      </Dialog>
      <Button onClick={open}>Editar</Button>
    </>
  );
}

const dateFormatter = new Intl.DateTimeFormat(undefined, {
  dateStyle: 'medium',
});






export default function AlbumListView() {
  const [items, setItems] = useState([]);
  const callData = () => {
    AlbumService.listAlbum().then(function (data) {
      setItems(data);
    });
  }
  useEffect(() => {
    callData();
  }, []);

  function index({ model }: { model: GridItemModel<Album> }) {
    return (
      <span>
        {model.index + 1}
      </span>
    );
  }

  function link({ item }: { item: Album }) {

    return (
      <span>
        <AlbumEntryFormUpdate arguments={item} onAlbumUpdate={callData}>
        </AlbumEntryFormUpdate>
      </span>
    );
  }

  /*   const dataProvider = useDataProvider<Cancion>({
    list: () => CancionService.listAllCancion(),
  }); */

  const order = (event, columnId) => {
    console.log(event);
    const direction = event.detail.direction;
    var dir = (direction === 'asc') ? 1 : 2;
    AlbumService.order(columnId, dir).then(function (data) {
      setItems(data);
    });
  }


  // 1. Estado para bandas y álbumes
  const [bandas, setBandas] = useState<{ id: string, label: string }[]>([]);

  // 2. Cargar bandas al montar el componente
  useEffect(() => {
    AlbumService.ListaBandaCombo()
      .then((result) => setBandas(result.map(b => ({ id: b.id, label: b.label }))))
      .catch(console.error);
  }, []);

  // 3. Función para obtener el nombre de la banda
  const getBandaLabel = (id_banda: number | string) => {
    const banda = bandas.find(b => b.id === id_banda?.toString());
    return banda ? banda.label : id_banda;
  };



  // Para buscar
  const criterio = useSignal('');
  const texto = useSignal('');

  const itemSelect = [
    {
      label: 'Nombre',
      value: 'nombre'
    },
    {
      label: 'Banda',
      value: 'banda'
    }
  ];



  const search = async () => {
    try {
      AlbumService.search(criterio.value, texto.value, 1).then(function (data) {
        setItems(data);
      });



      // Limpiar y cerrar
      criterio.value = '';
      texto.value = '';



      // Notificar y refrescar
      Notification.show('Busqueda realizada exitosamente',
        { duration: 5000, position: 'bottom-end', theme: 'success' });

    } catch (error) {
      console.error('Error al buscar álbumes:', error);
      Notification.show('Error al buscar álbumes',
        { duration: 5000, position: 'top-center', theme: 'error' });
      handleError(error);
    }
  };

  return (

    <main className="w-full h-full flex flex-col box-border gap-s p-m">
      <ViewToolbar title="Álbumes">
        <Group>
          <AlbumEntryForm onAlbumCreated={callData} />
        </Group>
      </ViewToolbar>
      <HorizontalLayout theme="spacing">
        <Select items={itemSelect}
          value={criterio.value}
          onValueChanged={(evt) => (criterio.value = evt.detail.value)}
          placeholder={'Seleccione'}>
        </Select>
        <TextField
          placeholder="Ingrese texto a buscar"
          style={{ width: '30%' }}
          value={texto.value}
          onValueChanged={(evt) => (texto.value = evt.detail.value)}
          onKeyDown={(evt) => {
            if (evt.key === 'Enter') {
              search();
            }
          }}


        >
          <Icon slot="prefix" icon="vaadin:search" />
        </TextField>
        <Button onClick={search} theme="primary">
          Buscar
        </Button>
      </HorizontalLayout>
      <Grid items={items}>
        <GridColumn renderer={index} header="Nro" />
        <GridSortColumn
          path={"nombre"}
          header="Nombre"
          onDirectionChanged={(e) => order(e, 'nombre')} />

        {/* 4. Mostrar el nombre de la banda */}
        <GridSortColumn
          path="id_banda"
          header="Banda"
          onDirectionChanged={(e) => order(e, 'id_banda')}
          renderer={({ item }) => <span>{getBandaLabel(item.id_banda)}</span>}
        />
        <GridSortColumn
          path="fecha"
          header="Fecha Lanzamiento"
          onDirectionChanged={(e) => order(e, 'fecha')}
          renderer={({ item }) => <span>{dateFormatter.format(new Date(item.fecha))}</span>}
        />
        <GridColumn header="Acciones" renderer={link} />
      </Grid>
    </main>
  );
}